package ru.practicum.android.diploma.details.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.Vacancy

interface DetailsInteractor {
    suspend fun searchDetails(id: String): Flow<Pair<Vacancy?, String?>>
}
