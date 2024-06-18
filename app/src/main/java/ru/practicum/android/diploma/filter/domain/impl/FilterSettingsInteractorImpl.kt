package ru.practicum.android.diploma.filter.domain.impl

import ru.practicum.android.diploma.filter.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.search.domain.model.Area
import ru.practicum.android.diploma.search.domain.model.Country
import ru.practicum.android.diploma.search.domain.model.Filter
import ru.practicum.android.diploma.search.domain.model.Industry

class FilterSettingsInteractorImpl(
    private val repository: FilterSettingsRepository
) : FilterSettingsInteractor {
    override fun getFilter(): Filter? = repository.getFilter()
    override fun updateIndustry(industry: Industry) { repository.updateIndustry(industry) }
    override fun clearIndustry() { repository.clearIndustry() }
    override fun updateSalary(salary: String) { repository.updateSalary(salary) }
    override fun clearSalary() { repository.clearSalary() }
    override fun updateCheckBox(isChecked: Boolean) { repository.updateCheckBox(isChecked) }
    override fun updateCountry(country: Country) { repository.updateCountry(country) }
    override fun clearCountry() { repository.clearCountry() }
    override fun updateArea(area: Area) { repository.updateArea(area) }
    override fun clearArea() { repository.clearArea() }
}
