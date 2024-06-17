package ru.practicum.android.diploma.filter.industry.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.filter.industry.domain.api.IndustriesRepository
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.search.data.model.IndustryDTO
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.sharing.data.ResourceProvider
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.Resource

class IndustriesRepositoryImpl(private val client: NetworkClient, private val resourceProvider: ResourceProvider) :
    IndustriesRepository {
    override suspend fun getIndustries(): Flow<Resource<List<Industry>>> = flow {
        val retrofitResponse = client.doSearchIndustriesRequest()
        val searchIndustriesResponseList = retrofitResponse.body()

        val industryDtoList = mutableListOf<IndustryDTO>()
        if (!searchIndustriesResponseList.isNullOrEmpty()) {
            searchIndustriesResponseList.forEach {
                industryDtoList.addAll(it.industries)
            }
        }
        val industryList = industryDtoList.map { industryDTO ->
            Industry(id = industryDTO.id, name = industryDTO.name)
        }

        when (retrofitResponse.code()) {
            Constants.SUCCESS -> emit(Resource.Success(industryList))
            Constants.SERVER_ERROR -> emit(Resource.Error(resourceProvider.getErrorServer()))
            Constants.CONNECTION_ERROR -> emit(Resource.Error(resourceProvider.getErrorInternetConnection()))
            else -> emit(Resource.Error(resourceProvider.getErrorServer()))
        }
    }
}
