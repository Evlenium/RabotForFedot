package ru.practicum.android.diploma.search.data.model

import com.google.gson.annotations.SerializedName

data class SimpleVacancyDTO(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("area") val address: SimpleAreaDTO?,
    @SerializedName("employer") val employer: EmployerDTO?,
    @SerializedName("salary") val salary: SalaryDTO?
)
