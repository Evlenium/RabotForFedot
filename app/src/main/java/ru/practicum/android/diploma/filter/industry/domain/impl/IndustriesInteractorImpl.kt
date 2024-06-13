package ru.practicum.android.diploma.filter.industry.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.filter.industry.domain.api.IndustriesInteractor
import ru.practicum.android.diploma.filter.industry.domain.api.IndustriesRepository
import ru.practicum.android.diploma.filter.industry.domain.model.Industry
import ru.practicum.android.diploma.util.Resource

class IndustriesInteractorImpl(private val industriesRepository: IndustriesRepository) : IndustriesInteractor {

    override suspend fun getIndustries(): Flow<Pair<List<Industry>?, String?>> {
        return industriesRepository.getIndustries().map { resource ->
            when (resource) {
                is Resource.Success -> Pair(resource.data, null)
                is Resource.Error -> Pair(null, resource.message)
            }
        }
    }
}
