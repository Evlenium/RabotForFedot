package ru.practicum.android.diploma.filter.area.data

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.filter.area.data.model.AreaDTO

data class SearchAreaResponse(
    val id: String,
    @SerializedName("areas") val childAreas: List<AreaDTO>?,
    @SerializedName("name") val parentRegionName: String,
    @SerializedName("parent_id") val parentId: String?
)
