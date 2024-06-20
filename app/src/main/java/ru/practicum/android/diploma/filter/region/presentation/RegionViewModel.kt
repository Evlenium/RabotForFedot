package ru.practicum.android.diploma.filter.region.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.filtration.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.region.presentation.model.RegionState
import ru.practicum.android.diploma.filter.workplace.domain.api.AreasInteractor
import ru.practicum.android.diploma.search.domain.model.Area
import ru.practicum.android.diploma.search.domain.model.Country

class RegionViewModel(
    private val searchAreasInteractor: AreasInteractor,
    private val filtrationInteractor: FilterSettingsInteractor,
) : ViewModel() {
    private val stateLiveData = MutableLiveData<RegionState>()
    private var parentId: String = ""
    private var countryList = mutableListOf<Country>()

    fun observeState(): LiveData<RegionState> = stateLiveData
    fun searchRequest() {
        renderState(RegionState.Loading)
        val countryName = filtrationInteractor.getFilter()?.countryName
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
            countryList.addAll(pair.first!!)
            val regions = ArrayList<Area>()
            countryList.forEach { country ->
                country.areas?.forEach { area ->
                    area?.let {
                        if (area.parentId != "1001") {
                            regions.add(it)
                        }
                    }
                }
            }
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

    fun getRegionCountry(): String {
        var stringRegionCountry = ""
        countryList.forEach { country ->
            if (country.id.equals(parentId)) {
                stringRegionCountry = country.name!!
                filtrationInteractor.updateCountry(country = country)
            }
        }
        return stringRegionCountry
    }

    fun setCountryFilter(area: Area) {
        filtrationInteractor.updateArea(area)
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
