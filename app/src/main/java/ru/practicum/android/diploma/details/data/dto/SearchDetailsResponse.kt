package ru.practicum.android.diploma.details.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.details.data.dto.model.AreaDTO
import ru.practicum.android.diploma.details.data.dto.model.ContactsDTO
import ru.practicum.android.diploma.details.data.dto.model.EmployerDTO
import ru.practicum.android.diploma.details.data.dto.model.EmploymentDTO
import ru.practicum.android.diploma.details.data.dto.model.ExperienceDTO
import ru.practicum.android.diploma.details.data.dto.model.KeySkillDTO
import ru.practicum.android.diploma.details.data.dto.model.SalaryDTO
import ru.practicum.android.diploma.search.data.dto.Response

data class SearchDetailsResponse(
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
