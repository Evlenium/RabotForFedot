package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.data.Resource
import ru.practicum.android.diploma.search.domain.model.Vacancy

interface SearchRepository {
    suspend fun searchVacancies(expression: String, filters: Map<String, String>): Flow<Resource<List<Vacancy>>>
}
