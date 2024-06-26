package ru.practicum.android.diploma.filter.workplace.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.Area
import ru.practicum.android.diploma.search.domain.model.Country
import ru.practicum.android.diploma.util.Resource

interface AreasRepository {
    suspend fun getAreas(): Flow<Resource<List<Country>>>

    suspend fun searchRegionByCountyName(countryName: String): Flow<Resource<List<Area>>>
}
