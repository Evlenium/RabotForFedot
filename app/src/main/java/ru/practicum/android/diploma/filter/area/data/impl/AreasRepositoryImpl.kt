package ru.practicum.android.diploma.filter.area.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.filter.area.domain.api.AreasRepository
import ru.practicum.android.diploma.filter.area.domain.model.ChildArea
import ru.practicum.android.diploma.filter.area.domain.model.ParentArea
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.sharing.data.ResourceProvider
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.Resource

class AreasRepositoryImpl(private val networkClient: NetworkClient, private val resourceProvider: ResourceProvider) :
    AreasRepository {
    override suspend fun getAreas(): Flow<Resource<List<ParentArea>>> = flow {
        val retrofitResponse = networkClient.doSearchAreaRequest()
        val searchAreaResponseList = networkClient.doSearchAreaRequest().body()
        val parentAreaList = searchAreaResponseList?.map { searchAreaResponse ->
            ParentArea(
                id = searchAreaResponse.id,
                childAreas = searchAreaResponse.childAreas?.map {
                    ChildArea(id = it.id, name = it.name, parentId = it.parentId)
                },
                parentRegionName = searchAreaResponse.parentRegionName,
                parentId = searchAreaResponse.parentId
            )
        }
        when (retrofitResponse.code()) {
            Constants.SUCCESS -> emit(Resource.Success(parentAreaList!!))
            Constants.SERVER_ERROR -> emit(Resource.Error(resourceProvider.getErrorServer()))
            Constants.CONNECTION_ERROR -> emit(Resource.Error(resourceProvider.getErrorInternetConnection()))
            else -> emit(Resource.Error(resourceProvider.getErrorServer()))
        }
    }
}
