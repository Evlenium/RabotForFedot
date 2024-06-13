package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.ServerVacanciesResponse

interface SearchInteractor {
    suspend fun searchVacancies(
        expression: String,
        filters: Map<String, String>
    ): Flow<Pair<ServerVacanciesResponse?, String?>>
}
