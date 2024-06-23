package ru.practicum.android.diploma.filter.region.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.region.presentation.model.RegionState
import ru.practicum.android.diploma.filter.workplace.domain.api.AreasInteractor
import ru.practicum.android.diploma.filter.workplace.domain.api.TemporarySharedInteractor
import ru.practicum.android.diploma.search.domain.model.Area
import ru.practicum.android.diploma.search.domain.model.Country
import ru.practicum.android.diploma.sharing.data.ResourceProvider

class RegionViewModel(
    private val searchAreasInteractor: AreasInteractor,
    private val temporarySharedInteractor: TemporarySharedInteractor,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val stateLiveData = MutableLiveData<RegionState>()
    private var parentId: String = ""
    private var countryList = mutableListOf<Country>()

    fun observeState(): LiveData<RegionState> = stateLiveData
    fun searchRequest() {
        renderState(RegionState.Loading)
        val countryName = temporarySharedInteractor.getWorkplace()?.countryName
        if (countryName != null) {
            searchRegionsCountry(countryName)
        } else {
            searchAllRegions()
        }
    }

    private fun searchRegionsCountry(countryName: String) {
        viewModelScope.launch {
            searchAreasInteractor
                .searchRegionsByCountryName(countryName)
                .collect { pair ->
                    bindState(pair)
                }
        }
    }

    private fun bindState(pair: Pair<List<Area>?, String?>) {
        val regions = ArrayList<Area>()
        if (pair.first != null) {
            regions.addAll(pair.first!!)
            renderState(
                RegionState.Content(
                    regions = regions
                )
            )
        }
        when {
            pair.second != null -> {
                renderState(
                    RegionState.Error(
                        errorMessage = pair.second!!
                    ),
                )
            }
        }
    }

    private fun searchAllRegions() {
        viewModelScope.launch {
            searchAreasInteractor
                .getAreas()
                .collect { pair ->
                    bindStateAllRegions(pair)
                }
        }
    }

    private fun bindStateAllRegions(pair: Pair<List<Country>?, String?>) {
        if (pair.first != null) {
            createContent(pair.first!!)
        }
        when {
            pair.second != null -> {
                renderState(
                    RegionState.Error(
                        errorMessage = pair.second!!
                    ),
                )
            }
        }
    }

    private fun createContent(first: List<Country>) {
        countryList.addAll(first)
        renderState(
            RegionState.Content(
                regions = updateCountryList(first)
            )
        )
    }

    private fun updateCountryList(countryList: List<Country>): ArrayList<Area> {
        val regions = ArrayList<Area>()
        countryList.forEach { country ->
            country.areas?.forEach { area ->
                if (area!!.parentId != "1001") {
                    regions.add(area)
                }
            }
        }
        return regions
    }

    fun getRegionCountry(): String {
        var stringRegionCountry = ""
        countryList.forEach { country ->
            if (country.id.equals(parentId)) {
                stringRegionCountry = country.name!!
                temporarySharedInteractor.updateCountry(country = country)
            }
        }
        return stringRegionCountry
    }

    fun getInternetConnection() = resourceProvider.checkInternetConnection()

    fun setCountryFilter(area: Area) {
        temporarySharedInteractor.updateRegion(area)
    }

    private fun renderState(state: RegionState) {
        stateLiveData.postValue(state)
    }

    fun setParentId(parentId: String?) {
        if (parentId != null) {
            this.parentId = parentId
        }
    }
}
