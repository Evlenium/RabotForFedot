package ru.practicum.android.diploma.search.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.details.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.filter.industry.data.dto.SearchIndustriesResponse
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.SearchRequest
import ru.practicum.android.diploma.sharing.data.ResourceProvider
import ru.practicum.android.diploma.util.Constants
import java.io.IOException

class RetrofitNetworkClient(
    private val service: SearchAPI,
    private val resourceProvider: ResourceProvider
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        return if (!resourceProvider.checkInternetConnection()) {
            Response().apply {
                result = Constants.CONNECTION_ERROR
            }
        } else {
            when (dto) {
                is SearchRequest -> doSearchRequest(dto)
                is VacancyDetailsRequest -> doSearchDetailsRequest(dto)
                else -> {
                    Response().apply { result = Constants.NOT_FOUND }
                }
            }
        }
    }

    private suspend fun doSearchRequest(searchRequest: SearchRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val searchResponse = service.getVacancies(searchRequest.expression, searchRequest.filters)
                searchResponse.apply { result = Constants.SUCCESS }
            } catch (exception: IOException) {
                Log.e("exception", "$exception")
                Response().apply { result = Constants.SERVER_ERROR }
            }
        }
    }

    private suspend fun doSearchDetailsRequest(vacancyDetailsRequest: VacancyDetailsRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val searchDetailsResponse = service.getVacancyDetails(vacancyDetailsRequest.id)
                searchDetailsResponse.apply { result = Constants.SUCCESS }
            } catch (exception: IOException) {
                Log.e("exception", "$exception")
                Response().apply { result = Constants.SERVER_ERROR }
            }
        }
    }

    override suspend fun doSearchIndustriesRequest(): retrofit2.Response<List<SearchIndustriesResponse>> {
        return withContext(Dispatchers.IO) {
            service.getIndustries()
        }
    }
}
