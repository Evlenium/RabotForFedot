package ru.practicum.android.diploma.search.data.network

import ru.practicum.android.diploma.filter.industry.data.dto.SearchIndustriesResponse
import ru.practicum.android.diploma.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
    suspend fun doSearchIndustriesRequest(): retrofit2.Response<List<SearchIndustriesResponse>>
}
