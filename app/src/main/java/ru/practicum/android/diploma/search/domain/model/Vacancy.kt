package ru.practicum.android.diploma.search.domain.model

data class Vacancy(
    val id: String,
    val name: String,
    val address: String?,
    val employer: Employer?,
    val salary: Salary?
)

