package ru.practicum.android.diploma.filter.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.Industry
import ru.practicum.android.diploma.util.Resource

interface FilterIndustryRepository {
    suspend fun getIndustries(): Flow<Resource<List<Industry>>>
}
