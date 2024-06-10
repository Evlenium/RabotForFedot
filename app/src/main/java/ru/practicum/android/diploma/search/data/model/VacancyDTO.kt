package ru.practicum.android.diploma.search.data.model

data class VacancyDTO(
    val id: String,
    val name: String,
    val address: AddressDTO?,
    val employer: EmployerDTO?,
    val salary: SalaryDTO?
)