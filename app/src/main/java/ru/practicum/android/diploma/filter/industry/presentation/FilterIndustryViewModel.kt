package ru.practicum.android.diploma.filter.industry.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.filtration.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.industry.domain.api.FilterIndustryInteractor
import ru.practicum.android.diploma.filter.industry.presentation.model.IndustriesState
import ru.practicum.android.diploma.filter.industry.presentation.model.IndustryState
import ru.practicum.android.diploma.search.domain.model.Industry
import ru.practicum.android.diploma.sharing.data.ResourceProvider

class FilterIndustryViewModel(
    private val industryInteractor: FilterIndustryInteractor,
    private val filterInteractor: FilterSettingsInteractor,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<IndustriesState>()
    private val stateLiveDataIndustry = MutableLiveData<IndustryState>()

    fun observeState(): LiveData<IndustriesState> = stateLiveData
    fun observeStateIndustry(): LiveData<IndustryState> = stateLiveDataIndustry

    fun searchRequest() {
        stateLiveData.postValue(IndustriesState.Loading)
        viewModelScope.launch {
            industryInteractor
                .getIndustries()
                .collect { pair ->
                    val industries = ArrayList<Industry>()
                    if (pair.first != null) {
                        industries.addAll(pair.first!!)
                        renderState(
                            IndustriesState.Content(
                                industries = industries
                            )
                        )
                    }
                    when {
                        pair.second != null -> {
                            renderState(
                                IndustriesState.Error(
                                    errorMessage = pair.second!!
                                )
                            )
                        }
                    }
                }
        }
    }

    fun getInternetConnection() = resourceProvider.checkInternetConnection()

    private fun renderState(state: IndustriesState) {
        stateLiveData.postValue(state)
    }

    fun saveIndustryFromAdapter(industry: Industry) {
        stateLiveDataIndustry.postValue(IndustryState.ContentIndustry(industry))
    }

    fun saveIndustry(industry: Industry) {
        filterInteractor.updateIndustry(industry = industry)
    }

    fun getIndustryId() = filterInteractor.getFilter()?.industryId
}
