package ru.practicum.android.diploma.filter.area.presentation.region.model

import ru.practicum.android.diploma.search.domain.model.Area

interface RegionState {
    object Loading : RegionState

    data class Content(
        val regions: ArrayList<Area>,
    ) : RegionState

    data class Error(
        val errorMessage: String,
    ) : RegionState
}
