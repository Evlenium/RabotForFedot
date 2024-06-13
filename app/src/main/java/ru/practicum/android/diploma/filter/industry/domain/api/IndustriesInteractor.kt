package ru.practicum.android.diploma.filter.industry.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.industry.domain.model.Industry

interface IndustriesInteractor {
    suspend fun getIndustries(): Flow<Pair<List<Industry>?, String?>>
}
