package ru.practicum.android.diploma.filter.area.domain.api

import kotlinx.coroutines.flow.Flow

import ru.practicum.android.diploma.filter.area.domain.model.Country
import ru.practicum.android.diploma.filter.area.domain.model.Region
import ru.practicum.android.diploma.util.Resource

interface AreasRepository {
    suspend fun getAreas(): Flow<Resource<List<Country>>>

    suspend fun getRegions(): Flow<Resource<List<Region>>>

    suspend fun searchRegionByCountyName(countryName: String): Flow<Resource<List<Region>>>
}
