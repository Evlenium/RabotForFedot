package ru.practicum.android.diploma.filter.filtration.data.api

interface FilterSettingsStorage {
    fun getFilter(): String
    fun updateFilter(editedFilter: String)
    fun clearFilter()
}
