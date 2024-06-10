package ru.practicum.android.diploma.details.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.details.domain.api.DetailsInteractor
import ru.practicum.android.diploma.details.domain.api.DetailsRepository
import ru.practicum.android.diploma.search.data.model.Vacancy
import ru.practicum.android.diploma.util.Resource

class DetailsInteractorImpl(private val repository: DetailsRepository) : DetailsInteractor {
    override suspend fun searchDetails(id: String): Flow<Pair<Vacancy?, String?>> {
        return repository.searchDetails(id).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }
}
