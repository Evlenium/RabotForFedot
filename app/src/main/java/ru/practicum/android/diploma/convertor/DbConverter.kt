package ru.practicum.android.diploma.convertor

import ru.practicum.android.diploma.favorite.data.db.FavoriteVacancyEntity
import ru.practicum.android.diploma.search.domain.model.SimpleVacancy
import ru.practicum.android.diploma.search.domain.model.Vacancy

class DbConverter {

    fun map(vacancy: Vacancy): FavoriteVacancyEntity = FavoriteVacancyEntity(
        id = vacancy.id.toInt(),
        address = vacancy.address,
        alternateUrl = vacancy.alternateUrl,
        contacts = vacancy.contacts,
        description = vacancy.description,
        employer = vacancy.employer,
        experience = vacancy.experience,
        keySkills = vacancy.keySkills,
        name = vacancy.name,
        professionalRoles = vacancy.professionalRoles,
        salary = vacancy.salary,
        schedule = vacancy.schedule,
        addingTime = System.currentTimeMillis(),
        area = vacancy.area
    )

    fun map(entity: FavoriteVacancyEntity): Vacancy = Vacancy(
        id = entity.id.toString(),
        address = entity.address,
        alternateUrl = entity.alternateUrl,
        contacts = entity.contacts,
        description = entity.description,
        employer = entity.employer,
        experience = entity.experience,
        keySkills = entity.keySkills,
        name = entity.name,
        professionalRoles = entity.professionalRoles,
        salary = entity.salary,
        schedule = entity.schedule,
        area = entity.area
    )

    private fun mapFavoriteToSimple(vacancy: Vacancy): SimpleVacancy = SimpleVacancy(
        id = vacancy.id,
        name = vacancy.name,
        address = vacancy.area,
        employer = vacancy.employer,
        salary = vacancy.salary
    )

    fun convertFromVacancyEntity(entityList: List<FavoriteVacancyEntity>): List<Vacancy> {
        return entityList.map { vacancyEntity -> map(vacancyEntity) }
    }

    fun convertFromFavoriteVacancy(vacancyList: List<Vacancy>): List<SimpleVacancy> {
        return vacancyList.map { favoriteVacancy -> mapFavoriteToSimple(favoriteVacancy) }
    }
}
