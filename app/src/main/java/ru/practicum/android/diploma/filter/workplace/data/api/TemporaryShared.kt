package ru.practicum.android.diploma.filter.workplace.data.api

interface TemporaryShared {
    fun getWorkplace(): String
    fun updateWorkplace(editedFilter: String)
    fun clearWorkplace()
}
