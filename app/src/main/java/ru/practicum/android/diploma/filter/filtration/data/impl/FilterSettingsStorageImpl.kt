package ru.practicum.android.diploma.filter.filtration.data.impl

import android.content.SharedPreferences
import ru.practicum.android.diploma.filter.filtration.data.api.FilterSettingsStorage

class FilterSettingsStorageImpl(
    private val prefs: SharedPreferences
) : FilterSettingsStorage {

    override fun getFilter(): String = prefs.getString(FILTER, "") ?: ""
    override fun updateFilter(editedFilter: String) { prefs.edit().putString(FILTER, editedFilter).apply() }
    override fun clearFilter() { prefs.edit().putString(FILTER, "").apply() }

    companion object {
        private const val FILTER = "FILTER"
    }
}
