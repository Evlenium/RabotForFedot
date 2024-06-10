package ru.practicum.android.diploma.search.data.network

import ru.practicum.android.diploma.details.data.dto.DetailsRequest
import ru.practicum.android.diploma.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
    suspend fun doSearchDetailsRequest(detailsRequest: DetailsRequest): Response
}
