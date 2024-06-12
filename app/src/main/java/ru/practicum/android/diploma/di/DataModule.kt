package ru.practicum.android.diploma.di

import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.convertor.DbConverter
import ru.practicum.android.diploma.convertor.ModelsConvertor
import ru.practicum.android.diploma.favorite.data.db.AppDatabase
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.data.network.SearchAPI
import ru.practicum.android.diploma.util.CheckConnection

val dataModule = module {

    factory { CheckConnection(get()) }

    single<SearchAPI> {
        Retrofit.Builder()
            .baseUrl("https://api.hh.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        chain.run {
                            proceed(
                                request()
                                    .newBuilder()
                                    .addHeader("Authorization", SearchAPI.TOKEN)
                                    .addHeader("HH-User-Agent", "RabotforFedot")
                                    .build()
                            )
                        }
                    }
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)
                    )
                    .build()
            )
            .build()
            .create(SearchAPI::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<NetworkClient> { RetrofitNetworkClient(service = get(), resourceProvider = get()) }
    single<ModelsConvertor> { ModelsConvertor() }
    single<DbConverter> { DbConverter() }
}
