package ru.practicum.android.diploma.filter.workplace.domain.impl

import ru.practicum.android.diploma.filter.workplace.domain.api.TemporarySharedInteractor
import ru.practicum.android.diploma.filter.workplace.domain.api.TemporarySharedRepository
import ru.practicum.android.diploma.filter.workplace.domain.model.Workplace
import ru.practicum.android.diploma.search.domain.model.Area
import ru.practicum.android.diploma.search.domain.model.Country

class TemporarySharedInteractorImpl(private val repository: TemporarySharedRepository) : TemporarySharedInteractor {
    override fun getWorkplace(): Workplace? = repository.getWorkplace()

    override fun clearWorkplace() = repository.clearWorkplace()

    override fun updateCountry(country: Country) = repository.updateCountry(country)

    override fun clearCountry() = repository.clearCountry()

    override fun updateRegion(area: Area) = repository.updateRegion(area)

    override fun clearArea() = repository.clearArea()
}
