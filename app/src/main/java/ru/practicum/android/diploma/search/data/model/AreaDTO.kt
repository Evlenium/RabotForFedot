package ru.practicum.android.diploma.search.data.model

import com.google.gson.annotations.SerializedName

data class AreaDTO(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("parent_id") val parentId: String?
)
