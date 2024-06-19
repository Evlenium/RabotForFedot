package ru.practicum.android.diploma.filter.industry.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.filter.industry.domain.api.FilterIndustryRepository
import ru.practicum.android.diploma.search.data.model.IndustryDTO
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.domain.model.Industry
import ru.practicum.android.diploma.util.Resource

class FilterIndustryRepositoryImpl(
    private val client: NetworkClient,
) : FilterIndustryRepository {

    override suspend fun getIndustries(): Flow<Resource<List<Industry>>> = flow {
        val response = client.doSearchIndustriesRequest()
        if (response.data != null) {
            emit(handleSuccessResponse(response.data))
        } else {
            response.message?.let { handleErrorResponse(it) }?.let { emit(it) }
        }
    }

    private fun handleErrorResponse(message: String): Resource<List<Industry>> {
        return Resource.Error(message)
    }

    private fun handleSuccessResponse(response: List<IndustryDTO>): Resource.Success<List<Industry>> {
        val industryList = response.map { industry ->
            Industry(
                id = industry.id,
                name = industry.name
            )
        }
        return Resource.Success(industryList)
    }
}
