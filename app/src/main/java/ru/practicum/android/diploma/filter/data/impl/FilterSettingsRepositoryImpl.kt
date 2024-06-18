package ru.practicum.android.diploma.filter.data.impl

import com.google.gson.Gson
import ru.practicum.android.diploma.filter.data.api.FilterSettingsStorage
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.search.domain.model.Area
import ru.practicum.android.diploma.search.domain.model.Country
import ru.practicum.android.diploma.search.domain.model.Filter
import ru.practicum.android.diploma.search.domain.model.Industry

class FilterSettingsRepositoryImpl(
    private val storage: FilterSettingsStorage,
    private val gson: Gson
) : FilterSettingsRepository {

    override fun getFilter(): Filter? {
        val filter = storage.getFilter()
        return if (filter.isEmpty() || filter == "null") {
            null
        } else if (checkFilter(gson.fromJson(filter, Filter::class.java))) {
            null
        } else {
            gson.fromJson(filter, Filter::class.java)
        }
    }

    fun checkFilter(filter: Filter): Boolean =
        with(filter) {
            isOnlyWithSalary == null &&
                countryName == null &&
                regionName == null &&
                regionId == null &&
                industryName == null &&
                industryId == null &&
                expectedSalary == null
        }

    override fun updateIndustry(industry: Industry) {
        val filter = getFilter()
        val updatedFilter = filter?.copy(industryId = industry.id, industryName = industry.name)
            ?: Filter(industryId = industry.id, industryName = industry.name)
        storage.updateFilter(gson.toJson(updatedFilter))
    }

    override fun clearIndustry() {
        val filter = getFilter()
        val clearedFilter = filter?.copy(industryName = null, industryId = null)
        storage.updateFilter(gson.toJson(clearedFilter))
    }

    override fun updateSalary(salary: String) {
        val filter = getFilter()
        val updatedFilter = filter?.copy(expectedSalary = salary.toLong())
        storage.updateFilter(gson.toJson(updatedFilter))
    }

    override fun clearSalary() {
        val filter = getFilter()
        val clearedFilter = filter?.copy(expectedSalary = null)
        storage.updateFilter(gson.toJson(clearedFilter))
    }

    override fun updateCheckBox(isChecked: Boolean) {
        val filter = getFilter()
        val updatedFilter = filter?.copy(isOnlyWithSalary = isChecked)
        storage.updateFilter(gson.toJson(updatedFilter))
    }

    override fun updateCountry(country: Country) {
        val filter = getFilter()
        val updatedFilter = filter?.copy(countryId = country.id, countryName = country.name)
            ?: Filter(countryId = country.id, countryName = country.name)
        storage.updateFilter(gson.toJson(updatedFilter))
    }

    override fun clearCountry() {
        val filter = getFilter()
        val clearedFilter = filter?.copy(countryName = null, countryId = null)
        storage.updateFilter(gson.toJson(clearedFilter))
    }

    override fun updateArea(area: Area) {
        val filter = getFilter()
        val updatedFilter = filter?.copy(regionId = area.id, regionName = area.name)
            ?: Filter(regionId = area.id, regionName = area.name)
        storage.updateFilter(gson.toJson(updatedFilter))
    }

    override fun clearArea() {
        val filter = getFilter()
        val clearedFilter = filter?.copy(regionName = null, regionId = null)
        storage.updateFilter(gson.toJson(clearedFilter))
    }
}
