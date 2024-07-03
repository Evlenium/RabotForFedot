package ru.practicum.android.diploma.search.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.convertor.ModelsConvertor
import ru.practicum.android.diploma.search.data.dto.SearchRequest
import ru.practicum.android.diploma.search.data.dto.SearchResponse
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.model.ServerVacanciesResponse
import ru.practicum.android.diploma.sharing.data.ResourceProvider
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.Resource

class SearchRepositoryImpl(
    private val client: NetworkClient,
    private val resourceProvider: ResourceProvider,
    private val convertor: ModelsConvertor
) : SearchRepository {
    override suspend fun searchVacancies(
        expression: String,
        filters: Map<String, String>
    ): Flow<Resource<ServerVacanciesResponse>> = flow {
        val response = client.doRequest(SearchRequest(expression, filters))

        when (response.result) {
            Constants.SUCCESS -> emit(
                Resource.Success(
                    ServerVacanciesResponse(
                        (response as SearchResponse).vacancies.map { vacancyDTO ->
                            convertor.map(vacancyDTO)
                        },
                        response.numberOfVacancies
                    )
                )
            )

            Constants.NOT_FOUND -> emit(Resource.Error(resourceProvider.getErrorEmptyListVacancy()))
            Constants.SERVER_ERROR -> emit(Resource.Error(resourceProvider.getErrorServer()))
            Constants.CONNECTION_ERROR -> emit(Resource.Error(resourceProvider.getErrorInternetConnection()))
        }
    }
}
