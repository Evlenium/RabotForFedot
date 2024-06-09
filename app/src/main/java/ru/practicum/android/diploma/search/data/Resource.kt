package ru.practicum.android.diploma.search.data

sealed class Resource<T>(val data: T? = null, val message: String? = null, val numberOfVacancies: Int? = null) {
    class Success<T>(data: T, numberOfVacancies: Int) : Resource<T>(data = data, numberOfVacancies = numberOfVacancies)
    class Error<T>(message: String) : Resource<T>(message = message)
}
