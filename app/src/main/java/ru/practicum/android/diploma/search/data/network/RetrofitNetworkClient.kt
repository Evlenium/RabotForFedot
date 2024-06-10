package ru.practicum.android.diploma.search.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.details.data.dto.DetailsRequest
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.SearchRequest
import ru.practicum.android.diploma.sharing.data.ResourceProvider
import ru.practicum.android.diploma.util.CheckConnection
import ru.practicum.android.diploma.util.Constants
import java.io.IOException

class RetrofitNetworkClient(
    private val service: SearchAPI,
    private val resourceProvider: ResourceProvider,
    private val checkConnection: CheckConnection
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        return if (!resourceProvider.checkInternetConnection()) {
            Response().apply {
                result = Constants.CONNECTION_ERROR
            }
        } else {
            when (dto) {
                is SearchRequest -> doSearchRequest(dto)
                is DetailsRequest -> doSearchDetailsRequest(dto)
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

    override suspend fun doSearchDetailsRequest(detailsRequest: DetailsRequest): Response {
        return when {
            !checkConnection.isInternetAvailable() -> {
                Response().apply { result = Constants.CONNECTION_ERROR }
            }

            else -> {
                withContext(Dispatchers.IO) {
                    try {
                        val searchDetailsResponse = service.getVacancyDetails(detailsRequest.id)
                        searchDetailsResponse.apply { result = Constants.SUCCESS }
                    } catch (exception: IOException) {
                        Log.e("exception", "$exception")
                        Response().apply { result = Constants.SERVER_ERROR }
                    }
                }
            }
        }
    }
}
