package ru.practicum.android.diploma.details.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.details.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.details.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.details.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.search.data.model.ContactsDTO
import ru.practicum.android.diploma.search.data.model.EmployerDTO
import ru.practicum.android.diploma.search.data.model.SalaryDTO
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.domain.model.Contacts
import ru.practicum.android.diploma.search.domain.model.Employer
import ru.practicum.android.diploma.search.domain.model.Phone
import ru.practicum.android.diploma.search.domain.model.Salary
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.sharing.data.ResourceProvider
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.Resource

class VacancyDetailsRepositoryImpl(
    private val client: NetworkClient,
    private val resourceProvider: ResourceProvider,
) : VacancyDetailsRepository {

    override suspend fun searchDetails(id: String): Flow<Resource<Vacancy>> = flow {
        val response = client.doRequest(VacancyDetailsRequest(id))
        when (response.result) {
            Constants.CONNECTION_ERROR -> emit(handleConnectionError())
            Constants.SUCCESS -> emit(handleSuccessResponse(response as VacancyDetailsResponse))
            else -> emit(handleServerError())
        }
    }

    private fun handleConnectionError(): Resource.Error<Vacancy> =
        Resource.Error(resourceProvider.getErrorInternetConnection())

    private fun handleServerError(): Resource.Error<Vacancy> =
        Resource.Error(resourceProvider.getErrorServer())

    private fun handleSuccessResponse(response: VacancyDetailsResponse): Resource.Success<Vacancy> {
        val vacancy = createVacancyFromResponse(response)
        return Resource.Success(vacancy)
    }

    private fun createVacancyFromResponse(response: VacancyDetailsResponse) = Vacancy(
        id = response.id,
        address = response.address?.city,
        alternateUrl = response.alternateUrl,
        area = response.area?.name,
        contacts = createContactsFromResponse(response.contacts),
        description = response.description,
        employer = createEmployerFromResponse(response.employer),
        experience = response.experience?.name,
        keySkills = response.keySkills?.map { it.name },
        name = response.name,
        professionalRoles = response.professionalRoles?.map { it.name },
        salary = createSalaryFromResponse(response.salary),
        schedule = response.schedule?.name
    )

    private fun createContactsFromResponse(contacts: ContactsDTO?) = Contacts(
        email = contacts?.email,
        name = contacts?.name,
        phones = contacts?.phones?.map { phone ->
            Phone(
                phone?.city,
                phone?.comment,
                phone?.country,
                phone?.number
            )
        }
    )

    private fun createEmployerFromResponse(employer: EmployerDTO?) = Employer(
        logoUrls = employer?.logoUrls?.original,
        name = employer?.name
    )

    private fun createSalaryFromResponse(salary: SalaryDTO?) = Salary(
        currency = salary?.currency,
        from = salary?.from,
        to = salary?.to
    )
}
