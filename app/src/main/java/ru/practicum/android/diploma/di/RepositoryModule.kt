package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.search.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.SearchRepository
import ru.practicum.android.diploma.sharing.data.ExternalNavigator
import ru.practicum.android.diploma.sharing.data.ResourceProvider

val repositoryModule = module {
    single<ExternalNavigator> { ExternalNavigator(context = androidContext()) }
    single<ResourceProvider> {
        ResourceProvider(
            context = androidContext(),
            checkConnection = get(),
        )
    }
    single<SearchRepository> { SearchRepositoryImpl(get(), get()) }
}
