package ru.practicum.android.diploma.search.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.details.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.filter.area.data.dto.SearchAreaResponse
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.SearchRequest
import ru.practicum.android.diploma.search.data.model.IndustryDTO
import ru.practicum.android.diploma.sharing.data.ResourceProvider
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.Resource
import java.io.IOException

class RetrofitNetworkClient(
    private val service: SearchAPI,
    private val resourceProvider: ResourceProvider,
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

    override suspend fun doSearchAreaRequest(): Resource<List<SearchAreaResponse>> {
        return when {
            !resourceProvider.checkInternetConnection() -> {
                Resource.Error(resourceProvider.getErrorInternetConnection())
            }

            else -> {
                withContext(Dispatchers.IO) {
                    try {
                        val response = service.getAreas().body()
                        val industriesList = mutableListOf<SearchAreaResponse>()
                        if (!response.isNullOrEmpty()) {
                            response.forEach { searchAreaResponse -> industriesList.add(searchAreaResponse) }
                        }
                        Resource.Success(industriesList)
                    } catch (exceptionArea: IOException) {
                        Log.e(AREA_EXCEPTION, "$exceptionArea")
                        Resource.Error(resourceProvider.getErrorServer())
                    }
                }
            }
        }
    }

    private suspend fun doSearchRequest(searchRequest: SearchRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val searchResponse = service.getVacancies(searchRequest.expression, searchRequest.filters)
                searchResponse.apply { result = Constants.SUCCESS }
            } catch (exceptionSearch: IOException) {
                Log.e(SEARCH_EXCEPTION, "$exceptionSearch")
                Response().apply { result = Constants.SERVER_ERROR }
            }
        }
    }

    private suspend fun doSearchDetailsRequest(vacancyDetailsRequest: VacancyDetailsRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val searchDetailsResponse = service.getVacancyDetails(vacancyDetailsRequest.id)
                searchDetailsResponse.apply { result = Constants.SUCCESS }
            } catch (exceptionDetails: IOException) {
                Log.e(DETAILS_EXCEPTION, "$exceptionDetails")
                Response().apply { result = Constants.SERVER_ERROR }
            }
        }
    }

    override suspend fun doSearchIndustriesRequest(): Resource<List<IndustryDTO>> {
        return when {
            !resourceProvider.checkInternetConnection() -> {
                Resource.Error(resourceProvider.getErrorInternetConnection())
            }

            else -> {
                withContext(Dispatchers.IO) {
                    try {
                        val response = service.getIndustries().body()
                        val industriesList = mutableListOf<IndustryDTO>()
                        if (!response.isNullOrEmpty()) {
                            response.forEach { searchIndustriesResponse ->
                                searchIndustriesResponse.industries.forEach { industry ->
                                    industriesList.add(industry)
                                }
                            }
                        }
                        Resource.Success(industriesList)
                    } catch (exceptionIndustries: IOException) {
                        Log.e(INDUSTRY_EXCEPTION, "$exceptionIndustries")
                        Resource.Error(resourceProvider.getErrorServer())
                    }
                }
            }
        }
    }

    companion object {
        const val SEARCH_EXCEPTION = "search_exception"
        const val DETAILS_EXCEPTION = "details_exception"
        const val AREA_EXCEPTION = "area_exception"
        const val INDUSTRY_EXCEPTION = "industry_exception"
    }
}
