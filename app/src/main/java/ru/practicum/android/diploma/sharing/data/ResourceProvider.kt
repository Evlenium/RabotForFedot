package ru.practicum.android.diploma.sharing.data

import android.content.Context
import android.content.SharedPreferences
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.util.CheckConnection

class ResourceProvider(
    private val context: Context,
    private val checkConnection: CheckConnection,
    private val sharedPreferences: SharedPreferences
) {
    fun getErrorInternetConnection(): String {
        return context.getString(R.string.no_internet)
    }

    fun getErrorEmptyListVacancy(): String {
        return context.getString(R.string.failed_to_get_list_of_vacancies)
    }

    fun getErrorServer(): String {
        return context.getString(R.string.server_error)
    }

    fun checkInternetConnection(): Boolean {
        return checkConnection.isInternetAvailable()
    }

    fun clearShared() {
        sharedPreferences.edit()
            .putString(
                EDIT_TEXT_KEY,
                ""
            )
            .apply()
    }

    fun addToShared(editTextString: String) {
        sharedPreferences.edit()
            .putString(
                EDIT_TEXT_KEY,
                editTextString
            )
            .apply()
    }

    fun getShared(): String? {
        return sharedPreferences.getString(EDIT_TEXT_KEY, null)
    }

    private companion object {
        const val EDIT_TEXT_KEY = "key_for_edit_text"
    }
}
