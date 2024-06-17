package ru.practicum.android.diploma.filter.area.domain.model

data class ParentArea(
    val id: String,
    val childAreas: List<ChildArea>?,
    val parentRegionName: String,
    val parentId: String?
)
