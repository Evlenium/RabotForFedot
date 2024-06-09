package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.model.ServerVacanciesResponse
import ru.practicum.android.diploma.util.Resource

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {
    override suspend fun searchVacancies(
        expression: String,
        filters: Map<String, String>
    ): Flow<Pair<ServerVacanciesResponse?, String?>> {
        return searchRepository.searchVacancies(expression, filters).map { resource ->
            when (resource) {
                is Resource.Success -> Pair(resource.data, null)
                is Resource.Error -> Pair(null, resource.message)
            }
        }
    }
}
