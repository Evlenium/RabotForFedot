package ru.practicum.android.diploma.filter.filtration.domain.api

import ru.practicum.android.diploma.search.domain.model.Area
import ru.practicum.android.diploma.search.domain.model.Country
import ru.practicum.android.diploma.search.domain.model.Filter
import ru.practicum.android.diploma.search.domain.model.Industry

interface FilterSettingsInteractor {
    fun getFilter(): Filter?
    fun updateIndustry(industry: Industry)
    fun clearIndustry()
    fun updateSalary(salary: String)
    fun clearSalary()
    fun updateCheckBox(isChecked: Boolean)
    fun updateCountry(country: Country)
    fun clearCountry()
    fun updateArea(area: Area)
    fun clearArea()
}
