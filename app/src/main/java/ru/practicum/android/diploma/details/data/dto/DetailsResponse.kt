package ru.practicum.android.diploma.details.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.search.data.model.ExperienceDTO
import ru.practicum.android.diploma.search.data.model.KeySkillDTO
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.model.AddressDTO
import ru.practicum.android.diploma.search.data.model.AreaDTO
import ru.practicum.android.diploma.search.data.model.ContactsDTO
import ru.practicum.android.diploma.search.data.model.EmployerDTO
import ru.practicum.android.diploma.search.data.model.ProfessionalRoleDTO
import ru.practicum.android.diploma.search.data.model.SalaryDTO
import ru.practicum.android.diploma.search.data.model.ScheduleDTO

data class DetailsResponse(
    @SerializedName("id") val id: String,
    @SerializedName("address") val address: AddressDTO?,
    @SerializedName("area") val area: AreaDTO?,
    @SerializedName("alternate_url") val alternateUrl: String?,
    @SerializedName("contacts") val contacts: ContactsDTO?,
    @SerializedName("description") val description: String?,
    @SerializedName("employer") val employer: EmployerDTO?,
    @SerializedName("experience") val experience: ExperienceDTO?,
    @SerializedName("key_skills") val keySkills: List<KeySkillDTO>?,
    @SerializedName("name") val name: String,
    @SerializedName("professional_roles") val professionalRoles: List<ProfessionalRoleDTO>?,
    @SerializedName("salary") val salary: SalaryDTO?,
    @SerializedName("schedule") val schedule: ScheduleDTO?
) : Response()
