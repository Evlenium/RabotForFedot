package ru.practicum.android.diploma.search.data.network

import ru.practicum.android.diploma.filter.area.data.dto.SearchAreaResponse
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.model.IndustryDTO
import ru.practicum.android.diploma.util.Resource

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
    suspend fun doSearchAreaRequest(): Resource<List<SearchAreaResponse>>
    suspend fun doSearchIndustriesRequest(): Resource<List<IndustryDTO>>
}
