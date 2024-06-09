package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.search.domain.SearchInteractor
import ru.practicum.android.diploma.search.domain.SearchRepository
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.search.data.Resource

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {
    override suspend fun searchVacancies(
        expression: String,
        filters: Map<String, String>
    ): Flow<Pair<List<Vacancy>?, String?>> {
        return searchRepository.searchVacancies(expression, filters).map { resource ->
            when (resource) {
                is Resource.Success -> Pair(resource.data, resource.numberOfVacancies.toString())
                is Resource.Error -> Pair(null, resource.message)
            }
        }
    }

    /* override suspend fun getNumbersOfVacancies(
        expression: String,
        filters: Map<String, String>
    ): Flow<Int?> {
       return searchRepository.searchVacancies(expression, filters).map { resource ->
           when (resource){
               is Resource.Success -> resource.numberOfVacancies
               is Resource.Error -> null
           }
       }
    }
     */
}
