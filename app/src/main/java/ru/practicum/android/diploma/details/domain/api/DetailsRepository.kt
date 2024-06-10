package ru.practicum.android.diploma.details.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.Resource

interface DetailsRepository {
    suspend fun searchDetails(id: String): Flow<Resource<Vacancy>>
}
