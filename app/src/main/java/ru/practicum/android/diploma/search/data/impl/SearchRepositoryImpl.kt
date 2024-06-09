package ru.practicum.android.diploma.search.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.search.data.dto.SearchRequest
import ru.practicum.android.diploma.search.data.dto.SearchResponse
import ru.practicum.android.diploma.search.data.dto.model.EmployerDTO
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.domain.SearchRepository
import ru.practicum.android.diploma.search.domain.model.Employer
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.sharing.data.ResourceProvider
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.Resource

/*Не знал куда передавать получаемые данные SearchResponse (page, pages, numberOfVacancies),
     возможно флоу помимо списка вакансий  должен передавать эти данные,
      но, глядя на макеты, как будто нам это не нужно :)  */

class SearchRepositoryImpl(private val networkClient: NetworkClient, private val resourceProvider: ResourceProvider) :
    SearchRepository {
    override suspend fun searchVacancies(
        expression: String,
        filters: Map<String, String>
    ): Flow<Resource<List<Vacancy>>> = flow {
        val response = networkClient.doRequest(SearchRequest(expression, filters))
        when (response.result) {
            Constants.SUCCESS -> emit(Resource.Success((response as SearchResponse).vacancies.map { vacancyDTO ->
                Vacancy(
                    id = vacancyDTO.id,
                    name = vacancyDTO.name,
                    address = vacancyDTO.address?.city,
                    employer = map(vacancyDTO.employer),
                    salaryCurrency = vacancyDTO.salary?.currency,
                    salaryFrom = vacancyDTO.salary?.from,
                    salaryTo = vacancyDTO.salary?.to
                )
            }))

            Constants.NOT_FOUND -> emit(Resource.Error(resourceProvider.getErrorEmptyListVacancy()))
            Constants.SERVER_ERROR -> emit(Resource.Error(resourceProvider.getErrorServer()))
            Constants.CONNECTION_ERROR -> emit(Resource.Error(resourceProvider.getErrorInternetConnection()))
        }
    }

    /* возможно, стоит создать отдельный класс маппер, но это предположение,
       а пока оставил так
     */
    private fun map(employerDTO: EmployerDTO?): Employer {
        return Employer(
            employerDTO?.name,
            employerDTO?.logoUrls?.n90,
            employerDTO?.logoUrls?.n240,
            employerDTO?.logoUrls?.original
        )
    }
}
