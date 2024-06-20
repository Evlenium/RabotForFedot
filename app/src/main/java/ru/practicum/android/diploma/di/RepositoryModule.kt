package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.details.data.impl.VacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.details.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.favorite.data.impl.FavoriteVacancyRepositoryImpl
import ru.practicum.android.diploma.favorite.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.filter.workplace.data.impl.AreasRepositoryImpl
import ru.practicum.android.diploma.filter.workplace.domain.api.AreasRepository
import ru.practicum.android.diploma.filter.filtration.data.impl.FilterSettingsRepositoryImpl
import ru.practicum.android.diploma.filter.filtration.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.filter.industry.data.impl.FilterIndustryRepositoryImpl
import ru.practicum.android.diploma.filter.industry.domain.api.FilterIndustryRepository
import ru.practicum.android.diploma.search.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.sharing.data.ExternalNavigator
import ru.practicum.android.diploma.sharing.data.ResourceProvider

val repositoryModule = module {
    single<ExternalNavigator> { ExternalNavigator(context = androidContext()) }
    single<ResourceProvider> {
        ResourceProvider(
            context = androidContext(),
            checkConnection = get(),
            sharedPreferences = get()
        )
    }
    single<SearchRepository> { SearchRepositoryImpl(client = get(), resourceProvider = get(), convertor = get()) }
    single<VacancyDetailsRepository> { VacancyDetailsRepositoryImpl(client = get(), resourceProvider = get()) }
    single<FavoriteVacancyRepository> { FavoriteVacancyRepositoryImpl(appDatabase = get(), dbConverter = get()) }
    single<FilterSettingsRepository> { FilterSettingsRepositoryImpl(storage = get(), gson = get()) }
    single<FilterIndustryRepository> { FilterIndustryRepositoryImpl(client = get()) }
    single<AreasRepository> { AreasRepositoryImpl(client = get()) }
}
