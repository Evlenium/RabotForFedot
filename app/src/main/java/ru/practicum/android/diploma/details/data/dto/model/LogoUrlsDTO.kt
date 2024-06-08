package ru.practicum.android.diploma.details.data.dto.model

import com.google.gson.annotations.SerializedName

data class LogoUrlsDTO(
    @SerializedName("90") val size90: String,
    @SerializedName("240") val size240: String,
    @SerializedName("original") val sizeOriginal: String
)
