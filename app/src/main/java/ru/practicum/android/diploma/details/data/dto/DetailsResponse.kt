package ru.practicum.android.diploma.details.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.details.data.model.AreaDTO
import ru.practicum.android.diploma.details.data.model.ContactsDTO
import ru.practicum.android.diploma.details.data.model.EmployerDTO
import ru.practicum.android.diploma.details.data.model.EmploymentDTO
import ru.practicum.android.diploma.details.data.model.ExperienceDTO
import ru.practicum.android.diploma.details.data.model.KeySkillDTO
import ru.practicum.android.diploma.details.data.model.SalaryDTO
import ru.practicum.android.diploma.search.data.dto.Response

data class DetailsResponse(
    val id: String,
    @SerializedName("name") val vacancyTitle: String,
    val salary: SalaryDTO?,
    val employer: EmployerDTO?,
    val experience: ExperienceDTO?,
    val employment: EmploymentDTO?,
    val area: AreaDTO?,
    val description: String?,
    @SerializedName("key_skills") val keySkills: List<KeySkillDTO>?,
    val contacts: ContactsDTO?
) : Response()
