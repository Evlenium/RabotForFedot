package ru.practicum.android.diploma.convertor

import ru.practicum.android.diploma.search.data.model.EmployerDTO
import ru.practicum.android.diploma.search.data.model.SalaryDTO
import ru.practicum.android.diploma.search.data.model.VacancyDTO
import ru.practicum.android.diploma.search.domain.model.Employer
import ru.practicum.android.diploma.search.domain.model.Salary
import ru.practicum.android.diploma.search.domain.model.Vacancy

class ModelsConvertor {
    fun map(vacancyDTO: VacancyDTO): Vacancy {
        return Vacancy(
            id = vacancyDTO.id,
            name = vacancyDTO.name,
            address = vacancyDTO.address?.city,
            employer = map(vacancyDTO.employer),
            salary = vacancyDTO.salary?.let { map(it) }
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
