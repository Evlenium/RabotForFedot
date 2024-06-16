package ru.practicum.android.diploma.filter.area.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.area.domain.model.ParentArea
import ru.practicum.android.diploma.util.Resource

interface AreasRepository {
    suspend fun getAreas(): Flow<Resource<List<ParentArea>>>
}
