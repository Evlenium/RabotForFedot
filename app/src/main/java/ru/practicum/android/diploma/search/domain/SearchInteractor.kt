package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.Vacancy

interface SearchInteractor {
    suspend fun searchVacancies(expression: String, filters: Map<String, String>): Flow<Pair<List<Vacancy>?, String?>>
}
