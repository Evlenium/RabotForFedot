package ru.practicum.android.diploma.search.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.details.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.filter.industry.data.dto.SearchIndustriesResponse
import ru.practicum.android.diploma.filter.workplace.data.dto.SearchAreaResponse
import ru.practicum.android.diploma.search.data.dto.SearchResponse

interface SearchAPI {
    @GET("/vacancies?entity=vacancy")
    suspend fun getVacancies(
        @Query("text") searchRequest: String,
        @QueryMap filters: Map<String, String>,
    ): SearchResponse

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(@Path("vacancy_id") vacancyId: String): VacancyDetailsResponse

    @GET("/industries")
    suspend fun getIndustries(): Response<List<SearchIndustriesResponse>>

    @GET("/areas")
    suspend fun getAreas(): Response<List<SearchAreaResponse>>

    companion object {
        const val TOKEN = BuildConfig.HH_ACCESS_TOKEN
    }
}
