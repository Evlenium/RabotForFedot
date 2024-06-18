package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.convertor.DbConverter
import ru.practicum.android.diploma.convertor.ModelsConvertor
import ru.practicum.android.diploma.favorite.data.db.AppDatabase
import ru.practicum.android.diploma.filter.data.api.FilterSettingsStorage
import ru.practicum.android.diploma.filter.data.impl.FilterSettingsStorageImpl
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.data.network.SearchAPI
import ru.practicum.android.diploma.util.CheckConnection
import ru.practicum.android.diploma.util.Constants

val dataModule = module {

    factory { CheckConnection(get()) }

    single<SearchAPI> {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        chain.run {
                            proceed(
                                request()
                                    .newBuilder()
                                    .addHeader(Constants.AUTO_BEARER, SearchAPI.TOKEN)
                                    .addHeader(Constants.HH_USER_AGENT, Constants.APPLICATION_NAME)
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

    single { androidContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE) }
    single<FilterSettingsStorage> { FilterSettingsStorageImpl(prefs = get()) }
    factory { Gson() }
}
