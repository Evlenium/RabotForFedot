package ru.practicum.android.diploma.convertor

import ru.practicum.android.diploma.search.data.model.EmployerDTO
import ru.practicum.android.diploma.search.data.model.SalaryDTO
import ru.practicum.android.diploma.search.data.model.SimpleVacancyDTO
import ru.practicum.android.diploma.search.domain.model.Employer
import ru.practicum.android.diploma.search.domain.model.Salary
import ru.practicum.android.diploma.search.domain.model.SimpleVacancy

class ModelsConvertor {
    fun map(simpleVacancyDTO: SimpleVacancyDTO): SimpleVacancy {
        return SimpleVacancy(
            id = simpleVacancyDTO.id,
            name = simpleVacancyDTO.name,
            address = simpleVacancyDTO.address?.city,
            employer = map(simpleVacancyDTO.employer),
            salary = simpleVacancyDTO.salary?.let { map(it) }
        )
    }

    private fun map(employerDTO: EmployerDTO?): Employer {
        return Employer(
            logoUrls = employerDTO?.logoUrls?.original,
            name = employerDTO?.name
        )
    }

    private fun map(salaryDTO: SalaryDTO): Salary = Salary(
        currency = salaryDTO.currency,
        from = salaryDTO.from,
        to = salaryDTO.to
    )
}
