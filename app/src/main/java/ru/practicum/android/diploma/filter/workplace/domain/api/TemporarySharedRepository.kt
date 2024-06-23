package ru.practicum.android.diploma.filter.workplace.domain.api

import ru.practicum.android.diploma.filter.workplace.domain.model.Workplace
import ru.practicum.android.diploma.search.domain.model.Area
import ru.practicum.android.diploma.search.domain.model.Country

interface TemporarySharedRepository {
    fun getWorkplace(): Workplace?
    fun clearWorkplace()
    fun updateCountry(country: Country)
    fun clearCountry()
    fun updateRegion(area: Area)
    fun clearArea()
}
