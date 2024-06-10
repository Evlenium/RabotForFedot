package ru.practicum.android.diploma.search.data.model

import com.google.gson.annotations.SerializedName

data class EmployerDTO(
    @SerializedName("logo_urls") val logoUrls: LogoUrlsDTO?,
    @SerializedName("name") val name: String?
)
