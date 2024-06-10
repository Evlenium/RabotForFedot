package ru.practicum.android.diploma.details.presentation

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.details.domain.api.DetailsInteractor

class DetailsViewModel(
    private val detailsInteractor: DetailsInteractor
) : ViewModel()
