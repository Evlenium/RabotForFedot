package ru.practicum.android.diploma.search.domain.model

import ru.practicum.android.diploma.search.domain.model.Phone

data class Contacts(
    val email: String?,
    val name: String?,
    val phones: List<Phone>?
)
