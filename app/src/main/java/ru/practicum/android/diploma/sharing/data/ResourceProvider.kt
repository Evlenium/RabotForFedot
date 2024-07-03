package ru.practicum.android.diploma.sharing.data

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.util.CheckConnection

class ResourceProvider(
    private val context: Context,
    private val checkConnection: CheckConnection
) {
    fun getErrorInternetConnection() = context.getString(R.string.no_internet)
    fun getErrorEmptyListVacancy() = context.getString(R.string.failed_to_get_list_of_vacancies)
    fun getErrorServer() = context.getString(R.string.server_error)
    fun checkInternetConnection() = checkConnection.isInternetAvailable()
}
