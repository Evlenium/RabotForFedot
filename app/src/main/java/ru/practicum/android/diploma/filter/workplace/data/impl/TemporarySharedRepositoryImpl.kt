package ru.practicum.android.diploma.filter.workplace.data.impl

import com.google.gson.Gson
import ru.practicum.android.diploma.filter.workplace.data.api.TemporaryShared
import ru.practicum.android.diploma.filter.workplace.domain.api.TemporarySharedRepository
import ru.practicum.android.diploma.filter.workplace.domain.model.Workplace
import ru.practicum.android.diploma.search.domain.model.Area
import ru.practicum.android.diploma.search.domain.model.Country

class TemporarySharedRepositoryImpl(
    private val storage: TemporaryShared,
    private val gson: Gson,
) : TemporarySharedRepository {
    override fun getWorkplace(): Workplace? {
        val workplace = storage.getWorkplace()
        return if (workplace.isEmpty() || workplace == "null") {
            null
        } else {
            gson.fromJson(workplace, Workplace::class.java)
        }
    }

    override fun clearWorkplace() {
        val workplace = getWorkplace()
        val clearedFilter = workplace?.copy(countryName = null, countryId = null)
        storage.updateWorkplace(gson.toJson(clearedFilter))
    }

    override fun updateCountry(country: Country) {
        val workplace = getWorkplace()
        val updatedFilter = workplace?.copy(countryId = country.id, countryName = country.name)
            ?: Workplace(countryId = country.id, countryName = country.name)
        storage.updateWorkplace(gson.toJson(updatedFilter))
    }

    override fun clearCountry() {
        val workplace = getWorkplace()
        val clearedFilter = workplace?.copy(countryName = null, countryId = null)
        storage.updateWorkplace(gson.toJson(clearedFilter))
    }

    override fun updateRegion(area: Area) {
        val workplace = getWorkplace()
        val updatedFilter = workplace?.copy(regionId = area.id, regionName = area.name)
            ?: Workplace(regionId = area.id, regionName = area.name)
        storage.updateWorkplace(gson.toJson(updatedFilter))
    }

    override fun clearArea() {
        val workplace = getWorkplace()
        val clearedFilter = workplace?.copy(regionName = null, regionId = null)
        storage.updateWorkplace(gson.toJson(clearedFilter))
    }

    override fun validateRegion(country: Country) {
        val workplace = getWorkplace()
        val clearedFilter = workplace?.copy(regionName = null, regionId = null)
        var validate = true
        country.areas?.forEach {
            validate = it?.id.equals(workplace?.regionId)
        }
        if (!validate) {
            storage.updateWorkplace(gson.toJson(clearedFilter))
        } else {
            return
        }
    }
}
