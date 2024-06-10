package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.details.domain.api.DetailsInteractor
import ru.practicum.android.diploma.details.domain.impl.DetailsInteractorImpl
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.sharing.domain.api.ResourceInteractor
import ru.practicum.android.diploma.sharing.domain.api.SharingInteractor
import ru.practicum.android.diploma.sharing.domain.impl.ResourceInteractorImpl
import ru.practicum.android.diploma.sharing.domain.impl.SharingInteractorImpl

val interactorModule = module {
    single<ResourceInteractor> { ResourceInteractorImpl(resourceProvider = get()) }
    single<SharingInteractor> { SharingInteractorImpl(externalNavigator = get()) }
    single<SearchInteractor> { SearchInteractorImpl(get()) }
    single<DetailsInteractor> { DetailsInteractorImpl(repository = get()) }
}
