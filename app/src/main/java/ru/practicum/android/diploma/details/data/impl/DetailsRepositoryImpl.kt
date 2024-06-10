package ru.practicum.android.diploma.details.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.details.data.dto.DetailsRequest
import ru.practicum.android.diploma.details.data.dto.DetailsResponse
import ru.practicum.android.diploma.details.domain.api.DetailsRepository
import ru.practicum.android.diploma.search.data.model.EmployerDTO
import ru.practicum.android.diploma.search.data.model.SalaryDTO
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.domain.model.Employer
import ru.practicum.android.diploma.search.domain.model.Salary
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.sharing.data.ResourceProvider
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.Resource

class DetailsRepositoryImpl(
    private val client: NetworkClient,
    private val resourceProvider: ResourceProvider
) : DetailsRepository {

    override suspend fun searchDetails(id: String): Flow<Resource<Vacancy>> = flow {
        val response = client.doRequest(DetailsRequest(id))
        when (response.result) {
            Constants.CONNECTION_ERROR -> emit(handleConnectionError())
            Constants.SUCCESS -> emit(handleSuccessResponse(response as DetailsResponse))
            else -> emit(handleServerError())
        }
    }

    private fun handleConnectionError(): Resource.Error<Vacancy> {
        return Resource.Error(resourceProvider.getErrorInternetConnection())
    }

    private fun handleServerError(): Resource.Error<Vacancy> {
        return Resource.Error(resourceProvider.getErrorServer())
    }

    private fun handleSuccessResponse(response: DetailsResponse): Resource.Success<Vacancy> {
        val vacancy = createVacancyFromResponse(response)
        return Resource.Success(vacancy)
    }

    private fun createVacancyFromResponse(response: DetailsResponse): Vacancy {
        return Vacancy(
            id = response.id,
            address = response.address?.city,
            employer = createEmployerFromResponse(response.employer),
            name = response.name,
            salary = createSalaryFromResponse(response.salary)
        )
    }

    private fun createEmployerFromResponse(employer: EmployerDTO?): Employer {
        return Employer(
            logoUrls = employer?.logoUrls?.original,
            name = employer?.name
        )
    }

    private fun createSalaryFromResponse(salary: SalaryDTO?): Salary {
        return Salary(
            currency = salary?.currency,
            from = salary?.from,
            to = salary?.to
        )
    }
}
