package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.details.data.dto.DetailsResponse
import ru.practicum.android.diploma.filter.area.data.SearchAreaResponse
import ru.practicum.android.diploma.filter.industry.data.SearchIndustriesResponse
import ru.practicum.android.diploma.search.data.dto.SearchResponse

interface SearchAPI {

    @Headers(
        "Authorization: Bearer $TOKEN",
        "HH-User-Agent: название приложения"
    )
    @GET("/vacancies?entity=vacancy")
    fun getVacancies(@Query("text") searchRequest: String, @QueryMap filters: Map<String, String>): SearchResponse

    @Headers(
        "Authorization: Bearer $TOKEN",
        "HH-User-Agent: название приложения"
    )
    @GET("/vacancies/{vacancy_id}")
    fun getVacancyDetails(@Path("vacancy_id") vacancyId: String): DetailsResponse

    @Headers(
        "Authorization: Bearer $TOKEN",
        "HH-User-Agent: название приложения"
    )
    @GET("/industries")
    fun getIndustries(): SearchIndustriesResponse

    @Headers(
        "Authorization: Bearer $TOKEN",
        "HH-User-Agent: название приложения"
    )
    @GET("/areas")
    fun getAreas(): List<SearchAreaResponse>

    companion object {
        const val TOKEN = "Здесь будет токен"
    }
}
