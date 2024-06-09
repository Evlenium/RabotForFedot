package ru.practicum.android.diploma.search.data

import ru.practicum.android.diploma.search.data.dto.model.EmployerDTO
import ru.practicum.android.diploma.search.data.dto.model.VacancyDTO
import ru.practicum.android.diploma.search.domain.model.Employer
import ru.practicum.android.diploma.search.domain.model.Vacancy

/* над названием класса возможно стоит подумать */
/*возможно стоит подобрать модификатор доступа аналогичный "protected" в java, но не уверен */
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
            employerDTO?.name,
            employerDTO?.logoUrls?.n90,
            employerDTO?.logoUrls?.n240,
            employerDTO?.logoUrls?.original
        )
    }
}
