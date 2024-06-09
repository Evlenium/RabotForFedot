package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.ServerVacanciesResponse
import ru.practicum.android.diploma.util.Resource

interface SearchRepository {
    suspend fun searchVacancies(expression: String, filters: Map<String, String>): Flow<Resource<ServerVacanciesResponse>>
}
