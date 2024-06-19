package ru.practicum.android.diploma.filter.area.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.area.domain.model.Country
import ru.practicum.android.diploma.filter.area.domain.model.Region

interface AreasInteractor {
    suspend fun getAreas(): Flow<Pair<List<Country>?, String?>>

    suspend fun getRegions(): Flow<Pair<List<Region>?, String?>>

    suspend fun searchRegionsByCountryName(countryName: String): Flow<Pair<List<Region>?, String?>>
}
