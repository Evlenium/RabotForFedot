package ru.practicum.android.diploma.favorite.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.convertor.DbConverter
import ru.practicum.android.diploma.favorite.data.db.AppDatabase
import ru.practicum.android.diploma.favorite.data.db.FavoriteVacancyEntity
import ru.practicum.android.diploma.favorite.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.search.domain.model.SimpleVacancy
import ru.practicum.android.diploma.search.domain.model.Vacancy

class FavoriteVacancyRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConverter: DbConverter,
) : FavoriteVacancyRepository {

    override suspend fun addVacancyToFavoriteList(vacancy: Vacancy) {
        val entity = dbConverter.map(vacancy)
        appDatabase.favoriteVacancyDao().addItem(entity)
    }

    override suspend fun removeVacancyFromFavoriteList(vacancy: Vacancy) {
        val entity = dbConverter.map(vacancy)
        appDatabase.favoriteVacancyDao().removeItem(entity)
    }

    override suspend fun getVacancyFromFavoriteList(id: String): Vacancy {
        val entity = appDatabase.favoriteVacancyDao().getItem(id.toInt())
        return dbConverter.map(entity)
    }

    override fun getAllFavoriteVacancies(): Flow<List<SimpleVacancy>> = flow {
        val entityList = appDatabase.favoriteVacancyDao().getAllItems()
        val itemList = convertFromVacancyEntity(entityList.sortedByDescending { it.addingTime })
        val simpleList = convertFromFavoriteVacancy(itemList)
        emit(simpleList)
    }

    override suspend fun isVacancyFavorite(id: String): Boolean {
        return appDatabase.favoriteVacancyDao().isItemExists(id.toInt())
    }

    private fun convertFromVacancyEntity(entityList: List<FavoriteVacancyEntity>): List<Vacancy> {
        return dbConverter.convertFromVacancyEntity(entityList)
    }

    private fun convertFromFavoriteVacancy(vacancyList: List<Vacancy>): List<SimpleVacancy> {
        return dbConverter.convertFromFavoriteVacancy(vacancyList)
    }
}
