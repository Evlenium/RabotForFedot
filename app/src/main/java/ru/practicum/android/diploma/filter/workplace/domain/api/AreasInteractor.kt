package ru.practicum.android.diploma.filter.workplace.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.Area
import ru.practicum.android.diploma.search.domain.model.Country

interface AreasInteractor {
    suspend fun getAreas(): Flow<Pair<List<Country>?, String?>>

    suspend fun searchRegionsByCountryName(countryName: String): Flow<Pair<List<Area>?, String?>>
}
