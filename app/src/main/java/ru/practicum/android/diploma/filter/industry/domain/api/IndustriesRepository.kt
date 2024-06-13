package ru.practicum.android.diploma.filter.industry.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.util.Resource

interface IndustriesRepository {
    suspend fun getIndustries(): Flow<Resource<List<Industry>>>
}
