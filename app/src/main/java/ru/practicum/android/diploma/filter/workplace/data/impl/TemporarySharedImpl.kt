package ru.practicum.android.diploma.filter.workplace.data.impl

import android.content.SharedPreferences
import ru.practicum.android.diploma.filter.workplace.data.api.TemporaryShared

class TemporarySharedImpl(private val sharedPreferences: SharedPreferences) : TemporaryShared {
    override fun getWorkplace(): String = sharedPreferences.getString(WORKPLACE, "") ?: ""
    override fun updateWorkplace(editedFilter: String) {
        sharedPreferences.edit().putString(WORKPLACE, editedFilter).apply()
    }

    override fun clearWorkplace() {
        sharedPreferences.edit().putString(WORKPLACE, "").apply()
    }

    companion object {
        private const val WORKPLACE = "workplace"
    }
}
