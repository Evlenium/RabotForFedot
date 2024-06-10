package ru.practicum.android.diploma.search.data.model

import ru.practicum.android.diploma.search.domain.model.Contacts
import ru.practicum.android.diploma.search.domain.model.Employer
import ru.practicum.android.diploma.search.domain.model.Salary

data class Vacancy(
    val id: String,
    val address: String?,
    val area: String?,
    val alternateUrl: String?,
    val contacts: Contacts?,
    val description: String?,
    val employer: Employer?,
    val experience: String?,
    val keySkills: List<String?>?,
    val name: String,
    val professionalRoles: List<String?>?,
    val salary: Salary?,
    val schedule: String?
)
