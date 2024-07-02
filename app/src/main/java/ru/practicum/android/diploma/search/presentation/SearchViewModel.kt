package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.filtration.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.model.FilterSearch
import ru.practicum.android.diploma.search.domain.model.SimpleVacancy
import ru.practicum.android.diploma.sharing.domain.api.ResourceInteractor
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val resourceInteractor: ResourceInteractor,
    private val filterSettingsInteractor: FilterSettingsInteractor
) : ViewModel() {
    var lastText: String = ""
    private var currentPage = 0
    private var maxPages = 0
    var totalVacanciesCount: Int = 0
    var flagSuccessfulDownload: Boolean = false
    private val options: HashMap<String, String> = HashMap()
    private var flagDebounce = false
    private var isNextPageLoading = false
    private var filterSearch: FilterSearch? = null

    private fun setOption(filterSearch: FilterSearch?) {
        maxPages = totalVacanciesCount / Constants.VACANCIES_PER_PAGE + 1

        if (totalVacanciesCount > Constants.VACANCIES_PER_PAGE && currentPage < maxPages) {
            options[Constants.PAGE] = currentPage.toString()
            options[Constants.PER_PAGE] = Constants.VACANCIES_PER_PAGE.toString()
        }

        if (filterSearch?.countryId != null) {
            options[Constants.AREA] = filterSearch.countryId
        } else {
            options.remove(Constants.AREA)
        }

        if (filterSearch?.regionId != null) {
            options[Constants.AREA] = filterSearch.regionId
        } else if (filterSearch?.countryId == null) {
            options.remove(Constants.AREA)
        }

        if (filterSearch?.industryId != null) {
            options[Constants.INDUSTRY] = filterSearch.industryId
        } else {
            options.remove(Constants.INDUSTRY)
        }

        if (filterSearch?.expectedSalary != null) {
            options[Constants.SALARY] = filterSearch.expectedSalary.toString()
        } else {
            options.remove(Constants.SALARY)
        }

        if (filterSearch?.isOnlyWithSalary != null && filterSearch.isOnlyWithSalary != false) {
            options[Constants.ONLY_WITH_SALARY] = filterSearch.isOnlyWithSalary.toString()
        } else {
            options.remove(Constants.ONLY_WITH_SALARY)
        }
    }

    private val stateLiveData = MutableLiveData<VacanciesState>()
    fun observeState(): LiveData<VacanciesState> = stateLiveData

    private val vacancySearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            run {
                SearchViewModel
                renderState(VacanciesState.Loading)
                searchRequest(changedText)
            }
        }

    fun searchDebounce(changedText: String) {
        if (changedText.trim().isEmpty()) {
            renderState(VacanciesState.Empty(message = resourceInteractor.getErrorEmptyListVacancy()))
            return
        }

        if (lastText != changedText) {
            currentPage = 0
            lastText = changedText
            flagDebounce = true
            vacancySearchDebounce(changedText)
            flagDebounce = false
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.trim().isEmpty()) {
            renderState(VacanciesState.Empty(message = resourceInteractor.getErrorEmptyListVacancy()))
            return
        }
        lastText = newSearchText
        viewModelScope.launch {
            searchInteractor
                .searchVacancies(newSearchText, options)
                .collect { pair ->
                    val vacancies = ArrayList<SimpleVacancy>()
                    if (pair.first != null) {
                        vacancies.addAll(pair.first!!.vacancies!!)
                        setContent(vacancies, pair.first!!.numberOfVacancies!!)
                        totalVacanciesCount = pair.first!!.numberOfVacancies!!
                    }
                    when {
                        pair.second != null -> {
                            setError(pair.second!!)
                        }

                        vacancies.isEmpty() -> {
                            setEmptyContent()
                        }
                    }
                }
            isNextPageLoading = false
        }
    }

    private fun setEmptyContent() {
        flagSuccessfulDownload = false
        renderState(
            VacanciesState.Empty(
                message = resourceInteractor.getErrorEmptyListVacancy()
            ),
        )
    }

    private fun setError(message: String) {
        flagSuccessfulDownload = false
        renderState(
            VacanciesState.Error(
                errorMessage = message
            ),
        )
    }

    private fun setContent(vacancies: ArrayList<SimpleVacancy>, numberOfVacancies: Int) {
        flagSuccessfulDownload = true
        renderState(
            VacanciesState.Content(
                vacancies = vacancies,
                numberOfVacancies = numberOfVacancies,
            )
        )
    }

    private fun renderState(state: VacanciesState) {
        stateLiveData.postValue(state)
    }

    fun uploadData() {
        if (resourceInteractor.checkInternetConnection()) {
            maxPages = totalVacanciesCount / Constants.VACANCIES_PER_PAGE + 1
            if (currentPage < maxPages && !isNextPageLoading) {
                isNextPageLoading = true
                currentPage++
                searchRequest(lastText)
                renderState(VacanciesState.BottomLoading)
            }
        } else {
            renderState(VacanciesState.ErrorToast(errorMessage = resourceInteractor.getErrorInternetConnection()))
        }
    }

    fun downloadData(request: String) {
        setOption(filterSearch)
        renderState(VacanciesState.Loading)
        if (!flagDebounce) searchRequest(request)
    }

    fun setFilterSearch(filterSearch: FilterSearch?) {
        this.filterSearch = filterSearch
    }

    fun createFilterFromShared(): FilterSearch {
        val industryId = filterSettingsInteractor.getFilter()?.industryId
        val onlyWithSalary = filterSettingsInteractor.getFilter()?.isOnlyWithSalary
        val countryId = filterSettingsInteractor.getFilter()?.countryId
        val regionId = filterSettingsInteractor.getFilter()?.regionId
        val salary = filterSettingsInteractor.getFilter()?.expectedSalary
        return FilterSearch(
            industryId = industryId,
            countryId = countryId,
            regionId = regionId,
            isOnlyWithSalary = onlyWithSalary,
            expectedSalary = salary
        )
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
