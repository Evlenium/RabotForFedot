package ru.practicum.android.diploma.search.data.convertor

import ru.practicum.android.diploma.search.data.model.EmployerDTO
import ru.practicum.android.diploma.search.data.model.VacancyDTO
import ru.practicum.android.diploma.search.domain.model.Employer
import ru.practicum.android.diploma.search.domain.model.Vacancy

class SearchModelsConvertor {
    fun map(vacancyDTO: VacancyDTO): Vacancy {
        return Vacancy(
            id = vacancyDTO.id,
            name = vacancyDTO.name,
            address = vacancyDTO.address?.city,
            employer = map(vacancyDTO.employer),
            salaryCurrency = vacancyDTO.salary?.currency,
            salaryFrom = vacancyDTO.salary?.from,
            salaryTo = vacancyDTO.salary?.to
        )
    }

    private fun map(employerDTO: EmployerDTO?): Employer {
        return Employer(
            name = employerDTO?.name,
            logoUrl90 = employerDTO?.logoUrls?.n90,
            logoUrl240 = employerDTO?.logoUrls?.n240,
            logoOriginal = employerDTO?.logoUrls?.original
        )
    }
}
