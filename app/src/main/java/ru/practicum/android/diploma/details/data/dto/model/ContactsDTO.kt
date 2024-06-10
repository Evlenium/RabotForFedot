package ru.practicum.android.diploma.details.data.dto.model

data class ContactsDTO(
    val email: String,
    val name: String,
    val phones: List<PhoneDTO>
)
