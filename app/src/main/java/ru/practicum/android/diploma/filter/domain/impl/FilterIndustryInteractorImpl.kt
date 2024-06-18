package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.filter.domain.api.FilterIndustryInteractor
import ru.practicum.android.diploma.filter.domain.api.FilterIndustryRepository
import ru.practicum.android.diploma.search.domain.model.Industry
import ru.practicum.android.diploma.util.Resource

class FilterIndustryInteractorImpl(
    private val repository: FilterIndustryRepository
) : FilterIndustryInteractor {

    override suspend fun getIndustries(): Flow<Pair<List<Industry>?, String?>> {
        return repository.getIndustries().map { resource ->
            when (resource) {
                is Resource.Success -> Pair(resource.data, null)
                is Resource.Error -> Pair(null, resource.message)
            }
        }
    }
}
