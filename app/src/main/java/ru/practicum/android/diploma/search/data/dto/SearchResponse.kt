package ru.practicum.android.diploma.search.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.search.data.model.VacancyDTO

data class SearchResponse(
    @SerializedName("items") val vacancies: List<VacancyDTO>,
    @SerializedName("found") val numberOfVacancies: Int,
    val page: Int,
    val pages: Int
) : Response()
