package ru.practicum.android.diploma.filter.industry.data.dto

import ru.practicum.android.diploma.search.data.model.IndustryDTO
import ru.practicum.android.diploma.search.data.dto.Response

data class SearchIndustriesResponse(val industries: List<IndustryDTO>): Response()
