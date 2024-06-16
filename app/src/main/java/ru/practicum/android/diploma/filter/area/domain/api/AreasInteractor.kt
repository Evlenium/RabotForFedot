package ru.practicum.android.diploma.filter.area.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.area.domain.model.ParentArea

interface AreasInteractor {
    suspend fun getAreas(): Flow<Pair<List<ParentArea>?, String?>>
}
