package ru.practicum.android.diploma.filter.area.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.area.domain.model.ChildArea
import ru.practicum.android.diploma.filter.area.domain.model.ParentArea
import ru.practicum.android.diploma.util.Resource

interface AreasRepository {
    suspend fun getAreas(): Flow<Resource<List<ParentArea>>>
    suspend fun getAllRegions(): Flow<Resource<List<ChildArea>>>
    suspend fun getRegions(parentId: String): Flow<Resource<List<ChildArea>>>
    suspend fun getOtherRegionsCountries(parentId: String): Flow<Resource<List<ChildArea>>>
}
