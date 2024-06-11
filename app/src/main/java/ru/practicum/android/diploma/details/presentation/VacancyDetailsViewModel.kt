package ru.practicum.android.diploma.details.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.details.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.details.presentation.model.StateLoadVacancy
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.sharing.domain.api.ResourceInteractor
import ru.practicum.android.diploma.sharing.domain.api.SharingInteractor

class VacancyDetailsViewModel(
    private val vacancyDetailsInteractor: VacancyDetailsInteractor,
    private val resourceInteractor: ResourceInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {
    private var currentVacancy: Vacancy? = null
    private var isFavorite: Boolean = false
    private val vacancyLiveData = MutableLiveData<StateLoadVacancy>()
    fun observeVacancy(): MutableLiveData<StateLoadVacancy> = vacancyLiveData
    private fun renderState(state: StateLoadVacancy) {
        vacancyLiveData.postValue(state)
    }

    fun searchRequest(id: String) {
        renderState(StateLoadVacancy.Loading)
        viewModelScope.launch {
            vacancyDetailsInteractor
                .searchDetails(id)
                .collect { pair ->
                    if (pair.first != null) {
                        currentVacancy = pair.first
                        renderState(StateLoadVacancy.Content(pair.first!!, isFavorite))
                    }
                    when {
                        pair.second != null -> {
                            renderState(StateLoadVacancy.Error(resourceInteractor.getErrorInternetConnection()))
                        }
                    }
                }
        }
    }

    fun callPhoneNumber(phone: String) {
        sharingInteractor.callPhoneNumber(phone)
    }

    fun writeToEmployer(mail: String) {
        sharingInteractor.writeToEmployer(mail)
    }

    fun shareVacancy(url: String) {
        sharingInteractor.shareVacancy(url)
    }
}
