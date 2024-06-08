package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.util.CheckConnection

val dataModule = module {
    factory { CheckConnection(get()) }
}
