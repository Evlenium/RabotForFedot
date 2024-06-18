package ru.practicum.android.diploma.search.presentation

import ru.practicum.android.diploma.search.domain.model.SimpleVacancy

sealed interface VacanciesState {
    object Loading : VacanciesState

    object BottomLoading : VacanciesState

    data class Content(
        val vacancies: ArrayList<SimpleVacancy>,
        val numberOfVacancies: Int,
    ) : VacanciesState

    data class Error(
        val errorMessage: String,
    ) : VacanciesState

    data class ErrorToast(
        val errorMessage: String,
    ) : VacanciesState

    data class Empty(
        val message: String,
    ) : VacanciesState
}
