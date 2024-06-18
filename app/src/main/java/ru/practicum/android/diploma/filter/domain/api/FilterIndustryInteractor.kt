package ru.practicum.android.diploma.filter.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.Industry

interface FilterIndustryInteractor {
    suspend fun getIndustries(): Flow<Pair<List<Industry>?, String?>>
}
