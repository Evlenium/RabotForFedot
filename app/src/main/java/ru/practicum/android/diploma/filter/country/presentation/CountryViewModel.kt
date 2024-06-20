package ru.practicum.android.diploma.filter.country.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.country.presentation.model.CountryState
import ru.practicum.android.diploma.filter.filtration.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.workplace.domain.api.AreasInteractor
import ru.practicum.android.diploma.search.domain.model.Country

class CountryViewModel(
    private val searchAreasInteractor: AreasInteractor,
    private val filtrationInteractor: FilterSettingsInteractor,
) : ViewModel() {
    private val stateLiveData = MutableLiveData<CountryState>()
    fun observeState(): LiveData<CountryState> = stateLiveData
    fun searchRequest() {
        renderState(CountryState.Loading)
        viewModelScope.launch {
            searchAreasInteractor
                .getAreas()
                .collect { pair ->
                    val countries = ArrayList<Country>()
                    if (pair.first != null) {
                        countries.addAll(pair.first!!)
                        renderState(
                            CountryState.Content(
                                countries = countries
                            )
                        )
                    }
                    when {
                        pair.second != null -> {
                            renderState(
                                CountryState.Error(
                                    errorMessage = pair.second!!
                                ),
                            )
                        }
                    }
                }
        }
    }

    fun setCountryFilter(country: Country) {
        filtrationInteractor.updateCountry(country)
    }

    private fun renderState(state: CountryState) {
        stateLiveData.postValue(state)
    }
}
