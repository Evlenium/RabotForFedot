package ru.practicum.android.diploma.details.data.dto.model

import com.google.gson.annotations.SerializedName

data class EmployerDTO(
    val id: String,
    @SerializedName("logo_urls") val logoUrls: LogoUrlsDTO,
    val name: String
)
