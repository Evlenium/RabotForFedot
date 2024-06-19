package ru.practicum.android.diploma.filter.area.domain.model

data class Country(
    val id: String,
    val regions: List<Region>?,
    val parentRegionName: String,
    val parentId: String?,
)
