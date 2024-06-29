package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.details.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.favorite.presentation.FavoriteViewModel
import ru.practicum.android.diploma.filter.country.presentation.CountryViewModel
import ru.practicum.android.diploma.filter.filtration.presentation.FilterSettingsViewModel
import ru.practicum.android.diploma.filter.industry.presentation.FilterIndustryViewModel
import ru.practicum.android.diploma.filter.region.presentation.RegionViewModel
import ru.practicum.android.diploma.filter.workplace.presentation.WorkplaceViewModel
import ru.practicum.android.diploma.search.presentation.SearchViewModel

val viewModelModule = module {
    viewModel { SearchViewModel(resourceInteractor = get(), searchInteractor = get(), filterSettingsInteractor = get()) }
    viewModel { FavoriteViewModel(favoriteInteractor = get()) }
    viewModel { FilterSettingsViewModel(filterSettingsInteractor = get(), temporarySharedInteractor = get()) }
    viewModel {
        FilterIndustryViewModel(
            industryInteractor = get(),
            filterInteractor = get(),
            resourceProvider = get()
        )
    }
    viewModel {
        VacancyDetailsViewModel(
            resourceInteractor = get(),
            sharingInteractor = get(),
            vacancyDetailsInteractor = get(),
            favoriteInteractor = get()
        )
    }
    viewModel {
        RegionViewModel(
            searchAreasInteractor = get(),
            temporarySharedInteractor = get(),
            resourceProvider = get()
        )
    }
    viewModel { CountryViewModel(searchAreasInteractor = get(), temporarySharedInteractor = get()) }
    viewModel { WorkplaceViewModel(filtrationInteractor = get(), temporarySharedInteractor = get()) }
}
