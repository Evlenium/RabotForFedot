package ru.practicum.android.diploma.search.data.dto

data class SearchRequest(val expression: String, val filters: Map<String, String>)
