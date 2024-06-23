package ru.practicum.android.diploma.search.domain.model

data class Country(
    val areas: List<Area?>?,
    val id: String?,
    val name: String?,
    val parentId: String?,
)
