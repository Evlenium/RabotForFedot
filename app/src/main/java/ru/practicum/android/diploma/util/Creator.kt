package ru.practicum.android.diploma.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.model.Salary
import java.text.NumberFormat
import java.util.Locale

object Creator {
    fun dpToPx(dp: Int): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

    fun getSalary(
        context: Context,
        salary: Salary?,
    ): String {
        val locale = Locale("ru")
        val numberFormat = NumberFormat.getInstance(locale)

        val currencySymbol = salary?.currency?.let { currencyCode ->
            val currency = Currency.fromCode(currencyCode)
            currency?.symbol ?: currencyCode
        } ?: ""

        return when {
            salary == null -> context.getString(R.string.salary_not_specified)
            salary.from != null && salary.to != null -> context.getString(
                R.string.salary_from_to,
                numberFormat.format(salary.from),
                numberFormat.format(salary.to),
                currencySymbol
            )

            salary.from != null -> context.getString(
                R.string.salary_from,
                numberFormat.format(salary.from),
                currencySymbol
            )

            salary.to != null -> context.getString(
                R.string.salary_to,
                numberFormat.format(salary.to),
                currencySymbol
            )

            else -> context.getString(R.string.salary_not_specified)
        }
    }
}
