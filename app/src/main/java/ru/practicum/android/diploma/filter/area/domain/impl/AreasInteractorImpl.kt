package ru.practicum.android.diploma.filter.area.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.filter.area.domain.api.AreasInteractor
import ru.practicum.android.diploma.filter.area.domain.api.AreasRepository
import ru.practicum.android.diploma.filter.area.domain.model.Country
import ru.practicum.android.diploma.filter.area.domain.model.Region
import ru.practicum.android.diploma.util.Resource

class AreasInteractorImpl(private val areasRepository: AreasRepository) : AreasInteractor {
    override suspend fun getAreas(): Flow<Pair<List<Country>?, String?>> {
        return areasRepository.getAreas().map { resource ->
            when (resource) {
                is Resource.Success -> Pair(resource.data, null)
                is Resource.Error -> Pair(null, resource.message)
            }
        }
    }
    override suspend fun getRegions(): Flow<Pair<List<Region>?, String?>> {
        return areasRepository.getRegions().map {
            when (it) {
                is Resource.Success -> Pair(it.data, null)
                is Resource.Error -> Pair(null, it.message)
            }
        }
    }

    override suspend fun searchRegionsByCountryName(countryName: String): Flow<Pair<List<Region>?, String?>> {
        return areasRepository.searchRegionByCountyName(countryName).map {
            when (it) {
                is Resource.Success -> Pair(it.data, null)
                is Resource.Error -> Pair(null, it.message)
            }
        }
    }
}
