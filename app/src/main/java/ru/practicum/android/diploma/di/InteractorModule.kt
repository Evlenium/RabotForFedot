package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.details.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.details.domain.impl.VacancyDetailsInteractorImpl
import ru.practicum.android.diploma.favorite.domain.api.FavoriteVacancyInteractor
import ru.practicum.android.diploma.favorite.domain.impl.FavoriteVacancyInteractorImpl
import ru.practicum.android.diploma.filter.area.domain.api.AreasInteractor
import ru.practicum.android.diploma.filter.area.domain.impl.AreasInteractorImpl
import ru.practicum.android.diploma.filter.industry.domain.api.IndustriesInteractor
import ru.practicum.android.diploma.filter.industry.domain.impl.IndustriesInteractorImpl
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.sharing.domain.api.ResourceInteractor
import ru.practicum.android.diploma.sharing.domain.api.SharingInteractor
import ru.practicum.android.diploma.sharing.domain.impl.ResourceInteractorImpl
import ru.practicum.android.diploma.sharing.domain.impl.SharingInteractorImpl

val interactorModule = module {
    factory<ResourceInteractor> { ResourceInteractorImpl(resourceProvider = get()) }
    factory<SharingInteractor> { SharingInteractorImpl(externalNavigator = get()) }
    factory<SearchInteractor> { SearchInteractorImpl(searchRepository = get()) }
    factory<VacancyDetailsInteractor> { VacancyDetailsInteractorImpl(repository = get()) }
    factory<FavoriteVacancyInteractor> { FavoriteVacancyInteractorImpl(repository = get()) }
    factory<IndustriesInteractor> { IndustriesInteractorImpl(industriesRepository = get()) }
    factory<AreasInteractor> { AreasInteractorImpl(areasRepository = get()) }
}
