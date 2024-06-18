package ru.practicum.android.diploma.search.data.network

import ru.practicum.android.diploma.filter.area.data.dto.SearchAreaResponse
import ru.practicum.android.diploma.filter.data.dto.SearchIndustriesResponse
import ru.practicum.android.diploma.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
    suspend fun doSearchAreaRequest(): retrofit2.Response<List<SearchAreaResponse>>
    suspend fun doSearchIndustriesRequest(): retrofit2.Response<List<SearchIndustriesResponse>>
}
