package ru.practicum.android.diploma.search.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.search.data.Resource
import ru.practicum.android.diploma.search.data.SearchModelsConvertor
import ru.practicum.android.diploma.search.data.dto.SearchRequest
import ru.practicum.android.diploma.search.data.dto.SearchResponse
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.domain.SearchRepository
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.sharing.data.ResourceProvider
import ru.practicum.android.diploma.util.Constants

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val resourceProvider: ResourceProvider,
    private val convertor: SearchModelsConvertor
) : SearchRepository {
    override suspend fun searchVacancies(
        expression: String,
        filters: Map<String, String>
    ): Flow<Resource<List<Vacancy>>> = flow {
        val response = networkClient.doRequest(SearchRequest(expression, filters))
        when (response.result) {
            Constants.SUCCESS -> emit(
                Resource.Success(
                    (response as SearchResponse).vacancies.map { vacancyDTO -> convertor.map(vacancyDTO) },
                    numberOfVacancies = response.numberOfVacancies
                )
            )
            Constants.NOT_FOUND -> emit(Resource.Error(resourceProvider.getErrorEmptyListVacancy()))
            Constants.SERVER_ERROR -> emit(Resource.Error(resourceProvider.getErrorServer()))
            Constants.CONNECTION_ERROR -> emit(Resource.Error(resourceProvider.getErrorInternetConnection()))
        }
    }
}
