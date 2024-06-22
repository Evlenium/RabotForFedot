package ru.practicum.android.diploma.filter.filtration.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.filtration.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.workplace.domain.api.TemporarySharedInteractor
import ru.practicum.android.diploma.search.domain.model.Filter
import ru.practicum.android.diploma.search.domain.model.FilterSearch

class FilterSettingsViewModel(
    private val filterSettingsInteractor: FilterSettingsInteractor,
    private val temporarySharedInteractor: TemporarySharedInteractor,
) : ViewModel() {
    val stateLiveDataFiltration = MutableLiveData<FullFilterState>()
    val stateLiveDataChanged = MutableLiveData<ChangeFilterState>()
    private val stateLiveDataArea = MutableLiveData<AreaState>()
    private val stateLiveDataSalary = MutableLiveData<SalaryState>()
    private val stateLiveDataIndustry = MutableLiveData<IndustryState>()
    val stateLiveDataCheckBox = MutableLiveData<CheckBoxState>()

    val salary by lazy(LazyThreadSafetyMode.NONE) {
        filterSettingsInteractor.getFilter()?.expectedSalary
    }

    private var industryIsEmpty = true
    private var workplaceIsEmpty = true
    private var salaryIsEmpty = true

    fun observeFiltrationState(): LiveData<FullFilterState> = stateLiveDataFiltration
    fun observeFiltrationStateChanged(): LiveData<ChangeFilterState> = stateLiveDataChanged
    fun observeAreaState(): LiveData<AreaState> = stateLiveDataArea
    fun observeIndustryState(): LiveData<IndustryState> = stateLiveDataIndustry
    fun observeCheckboxState(): LiveData<CheckBoxState> = stateLiveDataCheckBox

    private fun updateWorkplaceFromShared(country: String?, region: String?) {
        val stringCountryRegion: String
        if (country != null && region != null) {
            stringCountryRegion = "$country, $region"
            stateLiveDataArea.postValue(AreaState.WorkPlaceState(stringCountryRegion))
        } else if (country != null) {
            stringCountryRegion = "$country"
            stateLiveDataArea.postValue(AreaState.WorkPlaceState(stringCountryRegion))
        } else {
            stateLiveDataArea.postValue(AreaState.EmptyWorkplace)
        }
    }

    fun updateFilterParametersFromShared() {
        val industry = filterSettingsInteractor.getFilter()?.industryName
        val onlyWithSalary = filterSettingsInteractor.getFilter()?.isOnlyWithSalary
        val country = filterSettingsInteractor.getFilter()?.countryName
        val region = filterSettingsInteractor.getFilter()?.regionName
        updateWorkplaceFromShared(country, region)
        if (industry != null) setIndustry(industry)
        if (country != null) {
            if (region != null) {
                setWorkplace("$country, $region")
            } else {
                setWorkplace("$country")
            }
        }
        if (salary != null) setSalary(salary.toString())
        if (onlyWithSalary != null) {
            stateLiveDataCheckBox.postValue(CheckBoxState.IsCheck(onlyWithSalary))
        }
        if (checkOnNull(country, region, industry, salary, onlyWithSalary)) {
            stateLiveDataFiltration.postValue(FullFilterState.EmptyFilters)
        } else {
            stateLiveDataFiltration.postValue(FullFilterState.NonEmptyFilters)
        }
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

    private fun checkOnNull(
        country: String?,
        region: String?,
        industry: String?,
        salary: Long?,
        onlyWithSalary: Boolean?,
    ): Boolean {
        return country.isNullOrEmpty() && region.isNullOrEmpty() &&
            industry.isNullOrEmpty() && checkSalary(salary, onlyWithSalary)
    }

    private fun checkSalary(salary: Long?, onlyWithSalary: Boolean?): Boolean {
        return salary == null && onlyWithSalary == null || onlyWithSalary == false
    }

    private fun setWorkplace(workplace: String) {
        stateLiveDataArea.postValue(AreaState.WorkPlaceState(workplace))
        workplaceIsEmpty = false
    }

    fun clearWorkplace() {
        temporarySharedInteractor.clearCountry()
        temporarySharedInteractor.clearArea()
        filterSettingsInteractor.clearCountry()
        filterSettingsInteractor.clearArea()
        workplaceIsEmpty = true
        stateLiveDataArea.postValue(AreaState.EmptyWorkplace)
    }

    private fun setIndustry(industry: String) {
        stateLiveDataIndustry.postValue(IndustryState.FilterIndustryState(industry))
        industryIsEmpty = false
    }

    fun setIndustryIsEmpty() {
        filterSettingsInteractor.clearIndustry()
        industryIsEmpty = true
        stateLiveDataIndustry.postValue(IndustryState.EmptyFilterIndustry)
    }

    fun setSalary(inputTextFromApply: String) {
        salaryIsEmpty = false
        filterSettingsInteractor.updateSalary(inputTextFromApply)
        stateLiveDataSalary.postValue(SalaryState.FilterSalaryState(inputTextFromApply))
    }

    fun setSalaryIsEmpty() {
        salaryIsEmpty = true
        filterSettingsInteractor.clearSalary()
        stateLiveDataSalary.postValue(SalaryState.EmptyFilterSalary)
    }

    fun setCheckboxOnlyWithSalary(checkbox: Boolean) {
        filterSettingsInteractor.updateCheckBox(checkbox)
    }

    fun clearAllFilters() {
        industryIsEmpty = true
        workplaceIsEmpty = true
        salaryIsEmpty = true
        clearWorkplace()
        setIndustryIsEmpty()
        setSalaryIsEmpty()
        setCheckboxOnlyWithSalary(false)
        stateLiveDataCheckBox.postValue(CheckBoxState.IsCheck(false))
        stateLiveDataFiltration.postValue(FullFilterState.EmptyFilters)
    }

    fun checkFilters() {
        val industryWorkplaceIsEmpty = industryIsEmpty && workplaceIsEmpty
        if (industryWorkplaceIsEmpty
            && salaryIsEmpty
            && stateLiveDataCheckBox.value == CheckBoxState.IsCheck(false)
        ) {
            stateLiveDataFiltration.postValue(FullFilterState.EmptyFilters)
        } else {
            stateLiveDataFiltration.postValue(FullFilterState.NonEmptyFilters)
        }
    }

    fun setChangedState() {
        stateLiveDataChanged.postValue(ChangeFilterState.ChangedFilter)
    }

    fun setNoChangedState() {
        stateLiveDataChanged.postValue(ChangeFilterState.NoChangeFilters)
    }

    fun getIndustryFilterId(): String? {
        val filter = getFilter()
        return filter?.industryId
    }

    fun getFilter(): Filter? = filterSettingsInteractor.getFilter()
}
