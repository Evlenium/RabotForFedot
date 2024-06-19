package ru.practicum.android.diploma.filter.area.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.filter.area.data.dto.SearchAreaResponse
import ru.practicum.android.diploma.filter.area.domain.api.AreasRepository
import ru.practicum.android.diploma.filter.area.domain.model.Country
import ru.practicum.android.diploma.filter.area.domain.model.Region
import ru.practicum.android.diploma.search.data.model.AreaDTO
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.util.Resource

class AreasRepositoryImpl(private val client: NetworkClient) :
    AreasRepository {
    override suspend fun getAreas(): Flow<Resource<List<Country>>> = flow {
        val response = client.doSearchAreaRequest()
        if (response.data != null) {
            emit(handleSuccessResponse(response.data))
        } else {
            response.message?.let { handleErrorResponse(it) }?.let { emit(it) }
        }
    }

    override suspend fun getRegions(): Flow<Resource<List<Region>>> = flow {
        val responses = client.doSearchAreaRequest()
        if (responses.data != null) {
            responses.data.forEach { response ->
                if (response.id != "1001") {
                    emit(handleSuccessResponseRegion(response))
                }
            }
        } else {
            responses.message?.let { handleErrorResourceRegion(it) }?.let { emit(it) }
        }
    }

    private fun handleSuccessResponseRegion(response: SearchAreaResponse): Resource<List<Region>> {
        var areaList = emptyList<Region>()
        if (response.childAreas != null) {
            areaList = response.childAreas.map { createAreaFromResponse(it) }
        }
        return Resource.Success(areaList)
    }

    override suspend fun searchRegionByCountyName(countryName: String): Flow<Resource<List<Region>>> = flow {
        val responses = client.doSearchAreaRequest()
        if (responses.data != null) {
            responses.data.forEach { response ->
                if (response.childAreas?.equals(countryName) == true) {
                    emit(handleSuccessRegionByCountry(response))
                }
            }
        } else {
            responses.message?.let { handleErrorResourceRegion(it) }?.let { emit(it) }
        }
    }

    private fun handleSuccessRegionByCountry(response: SearchAreaResponse): Resource<List<Region>> {
        var areaList = emptyList<Region>()
        if (response.childAreas != null) {
            areaList = response.childAreas.map { createAreaFromResponse(it) }
        }
        return Resource.Success(areaList)
    }

    private fun handleErrorResourceRegion(message: String): Resource<List<Region>> {
        return Resource.Error(message)
    }

    private fun handleErrorResponse(message: String): Resource<List<Country>> {
        return Resource.Error(message)
    }

    private fun handleSuccessResponse(response: List<SearchAreaResponse>): Resource.Success<List<Country>> {
        val countryList = response.map { searchAreaResponse ->
            Country(
                id = searchAreaResponse.id,
                regions = searchAreaResponse.childAreas?.map {
                    Region(id = it.id, name = it.name, parentId = it.parentId)
                },
                parentRegionName = searchAreaResponse.parentRegionName,
                parentId = searchAreaResponse.parentId
            )
        }
        return Resource.Success(countryList)
    }

    private fun createAreaFromResponse(area: AreaDTO?): Region {
        return Region(
            id = area?.id,
            name = area?.name,
            parentId = area?.parentId
        )
    }
}
