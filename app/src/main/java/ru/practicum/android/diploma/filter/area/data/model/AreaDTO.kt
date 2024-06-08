package ru.practicum.android.diploma.filter.area.data.model

import com.google.gson.annotations.SerializedName

data class AreaDTO (
    val id: String,
    @SerializedName("name") val childRegionName: String,
    @SerializedName("parent_id") val parentId: String
)
