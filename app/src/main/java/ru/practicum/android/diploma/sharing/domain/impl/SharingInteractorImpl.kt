package ru.practicum.android.diploma.sharing.domain.impl

import ru.practicum.android.diploma.sharing.data.ExternalNavigator
import ru.practicum.android.diploma.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareVacancy(url: String) {
        externalNavigator.shareLink(url)
    }
}
