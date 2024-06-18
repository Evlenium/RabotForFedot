package ru.practicum.android.diploma.filter.area.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.area.domain.model.ChildArea
import ru.practicum.android.diploma.filter.area.domain.model.ParentArea

interface AreasInteractor {
    suspend fun getAreas(): Flow<Pair<List<ParentArea>?, String?>>
    suspend fun getAllRegions(): Flow<Pair<List<ChildArea>?, String?>>
    suspend fun getRegions(parentId: String): Flow<Pair<List<ChildArea>?, String?>>
    suspend fun getOtherRegionsCountries(parentId: String): Flow<Pair<List<ChildArea>?, String?>>
}
