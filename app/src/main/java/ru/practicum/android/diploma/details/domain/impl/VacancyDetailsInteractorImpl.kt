package ru.practicum.android.diploma.details.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.details.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.details.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.Resource

class VacancyDetailsInteractorImpl(private val repository: VacancyDetailsRepository) : VacancyDetailsInteractor {
    override suspend fun searchDetails(id: String): Flow<Pair<Vacancy?, String?>> {
        return repository.searchDetails(id).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }
}
