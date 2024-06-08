package ru.practicum.android.diploma.search.data.dto.model

import com.google.gson.annotations.SerializedName

data class EmployerDTO(
    @SerializedName("logo_urls") val logoUrls: LogoUrlDTO?,
    val name: String?
)
