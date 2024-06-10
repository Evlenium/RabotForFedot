package ru.practicum.android.diploma.search.data.model

import com.google.gson.annotations.SerializedName

data class ContactsDTO(
    @SerializedName("email") val email: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("phones") val phones: List<PhoneDTO?>
)
