package ru.practicum.android.diploma.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.details.data.dto.SearchDetailsRequest
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.SearchRequest
import java.io.IOException

/*нужно вынести в константы*/

const val BAD_REQUEST_STATUS_CODE = 400
const val NO_INTERNET = -1
const val OK_REQUEST = 200
const val INTERNAL_SERVER_ERROR = 500

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    /* searchAPI необходимо перенести в DataModules */
    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    private val searchAPI = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.hh.ru")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchAPI::class.java)

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply {
                result = NO_INTERNET
            }
        } else {
            return when (dto) {
                is SearchRequest -> doSearchRequest(dto)
                is SearchDetailsRequest -> doSearchDetailsRequest(dto)
                else -> {
                    Response().apply { result = BAD_REQUEST_STATUS_CODE }
                }
            }
        }
    }

    private suspend fun doSearchRequest(searchRequest: SearchRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val searchResponse = searchAPI.getVacancies(searchRequest.expression, searchRequest.filters)
                searchResponse.apply { result = OK_REQUEST }
            } catch (exception: IOException) {
                Response().apply { result = INTERNAL_SERVER_ERROR }
            }
        }
    }

    private suspend fun doSearchDetailsRequest(searchDetailsRequest: SearchDetailsRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val searchDetailsResponse = searchAPI.getVacancyDetails(searchDetailsRequest.id)
                searchDetailsResponse.apply { result = OK_REQUEST }
            } catch (exception: IOException) {
                Response().apply { result = INTERNAL_SERVER_ERROR }
            }
        }
    }

    /* метод isConnected() проверяет наличие интернета на устройстве*/
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}
