package ru.practicum.android.diploma.filter.presentation.industry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.details.presentation.model.StateLoadVacancy
import ru.practicum.android.diploma.filter.domain.api.FilterIndustryInteractor
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.presentation.industry.model.IndustriesState
import ru.practicum.android.diploma.filter.presentation.industry.model.IndustryState
import ru.practicum.android.diploma.search.domain.model.Industry

class FilterIndustryViewModel(
    private val industryInteractor: FilterIndustryInteractor,
    private val filterInteractor: FilterSettingsInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<IndustriesState>()
    private val stateLiveDataIndustry = MutableLiveData<IndustryState>()

    fun observeState(): LiveData<IndustriesState> = stateLiveData
    fun observeStateIndustry(): LiveData<IndustryState> = stateLiveDataIndustry

    fun searchRequest() {
        viewModelScope.launch {
            StateLoadVacancy.Loading
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

    private fun renderState(state: IndustriesState) {
        stateLiveData.postValue(state)
    }

    fun saveIndustryFromAdapter(industry: Industry) {
        stateLiveDataIndustry.postValue(IndustryState.ContentIndustry(industry))
    }

    fun saveIndustry(industry: Industry) {
        filterInteractor.updateIndustry(industry = industry)
    }
}
