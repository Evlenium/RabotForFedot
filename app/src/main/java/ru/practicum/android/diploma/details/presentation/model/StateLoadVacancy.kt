package ru.practicum.android.diploma.details.presentation.model

import ru.practicum.android.diploma.search.domain.model.Vacancy

sealed interface StateLoadVacancy {

    object Loading : StateLoadVacancy

    data class Content(
        val data: Vacancy,
        val isFavorite: Boolean,
    ) : StateLoadVacancy

    data class Error(
        val errorMessage: String,
    ) : StateLoadVacancy
}
