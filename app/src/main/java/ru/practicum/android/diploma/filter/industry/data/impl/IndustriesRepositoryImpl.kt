package ru.practicum.android.diploma.filter.industry.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.filter.industry.data.dto.SearchIndustriesRequest
import ru.practicum.android.diploma.filter.industry.data.dto.SearchIndustriesResponse
import ru.practicum.android.diploma.filter.industry.domain.api.IndustriesRepository
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.sharing.data.ResourceProvider
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.Resource

class IndustriesRepositoryImpl(private val client: NetworkClient, private val resourceProvider: ResourceProvider) :
    IndustriesRepository {
    override suspend fun getIndustries(): Flow<Resource<List<Industry>>> = flow {
        val response = client.doRequest(SearchIndustriesRequest)
        when (response.result) {
            Constants.SUCCESS -> emit(Resource.Success((response as SearchIndustriesResponse).industries.map {
                Industry(id = it.id, name = it.name)
            }))
            Constants.SERVER_ERROR -> emit(Resource.Error(resourceProvider.getErrorServer()))
            Constants.CONNECTION_ERROR -> emit(Resource.Error(resourceProvider.getErrorInternetConnection()))
        }
    }
}
