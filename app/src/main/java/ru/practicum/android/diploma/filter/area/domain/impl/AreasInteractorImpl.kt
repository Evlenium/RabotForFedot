package ru.practicum.android.diploma.filter.area.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.filter.area.domain.api.AreasInteractor
import ru.practicum.android.diploma.filter.area.domain.api.AreasRepository
import ru.practicum.android.diploma.filter.area.domain.model.ParentArea
import ru.practicum.android.diploma.util.Resource

class AreasInteractorImpl(private val areasRepository: AreasRepository) : AreasInteractor {
    override suspend fun getAreas(): Flow<Pair<List<ParentArea>?, String?>> {
        return areasRepository.getAreas().map { resource ->
            when (resource) {
                is Resource.Success -> Pair(resource.data, null)
                is Resource.Error -> Pair(null, resource.message)
            }
        }
    }
}
