package ru.practicum.android.diploma.filter.workplace.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.filtration.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.workplace.domain.api.TemporarySharedInteractor
import ru.practicum.android.diploma.filter.workplace.presentation.model.AreaState
import ru.practicum.android.diploma.search.domain.model.Area
import ru.practicum.android.diploma.search.domain.model.Country

class WorkplaceViewModel(
    private val filtrationInteractor: FilterSettingsInteractor,
    private val temporarySharedInteractor: TemporarySharedInteractor,
) : ViewModel() {
    private val stateLiveData = MutableLiveData<AreaState>()
    fun observeState(): LiveData<AreaState> = stateLiveData
    private var countryName: String? = null

    fun updateInfoFromShared() {
        val country = temporarySharedInteractor.getWorkplace()?.countryName
        val region = temporarySharedInteractor.getWorkplace()?.regionName
        if (country != null && region != null) {
            stateLiveData.postValue(AreaState.FullArea(country, region))
        } else if (country != null) {
            if (!countryName.isNullOrEmpty()) {
                stateLiveData.postValue(AreaState.CountryName(countryName!!))
            }
            stateLiveData.postValue(AreaState.CountryName(country))
        } else if (region != null) {
            if (!countryName.isNullOrEmpty()) {
                stateLiveData.postValue(AreaState.FullArea(countryName!!, region))
            } else {
                stateLiveData.postValue(AreaState.RegionName(region))
            }
        } else {
            stateLiveData.postValue(AreaState.Empty)
        }
    }

    fun updateFilterShared() {
        val workplace = temporarySharedInteractor.getWorkplace()
        filtrationInteractor.updateArea(Area(id = workplace?.regionId, name = workplace?.regionName, null))
        filtrationInteractor.updateCountry(
            Country(
                areas = null,
                id = workplace?.countryId,
                name = workplace?.countryName,
                null
            )
        )
    }

    fun isTheSameWorkplace(): Boolean {
        val workplace = temporarySharedInteractor.getWorkplace()
        val filter = filtrationInteractor.getFilter()
        return workplace?.countryId == filter?.countryId && workplace?.regionId == filter?.regionId
    }

    fun cleanCountryData() {
        temporarySharedInteractor.clearCountry()
        cleanRegionData()
    }

    fun cleanRegionData() {
        temporarySharedInteractor.clearArea()
        updateInfoFromShared()
    }

    fun setArgumentCountry(countryName: String) {
        this.countryName = countryName
    }
}
