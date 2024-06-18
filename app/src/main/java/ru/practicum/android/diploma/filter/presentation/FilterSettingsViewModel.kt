package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.search.domain.model.FilterSearch

class FilterSettingsViewModel(
    private val interactor: FilterSettingsInteractor
) : ViewModel() {
    private val stateLiveDataFiltration = MutableLiveData<FilterState>()
    private val stateLiveDataArea = MutableLiveData<AreaState>()
    private val stateLiveDataSalary = MutableLiveData<SalaryState>()
    private val stateLiveDataIndustry = MutableLiveData<IndustryState>()
    private val stateLiveDataCheckBox = MutableLiveData<CheckBoxState>()

    val salary by lazy(LazyThreadSafetyMode.NONE) {
        interactor.getFilter()?.expectedSalary
    }

    fun observeFiltrationState(): LiveData<FilterState> = stateLiveDataFiltration
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
        val industry = interactor.getFilter()?.industryName
        val onlyWithSalary = interactor.getFilter()?.isOnlyWithSalary
        val country = interactor.getFilter()?.countryName
        val region = interactor.getFilter()?.regionName

        updateWorkplaceFromShared(country, region)

        if (industry != null) setIndustry(industry)
        if (salary != null) setSalary(salary.toString())
        if (onlyWithSalary != null) {
            stateLiveDataCheckBox.postValue(CheckBoxState.IsCheck(onlyWithSalary))
        }
        if (checkOnNull(country, region, industry, salary, onlyWithSalary)) {
            stateLiveDataFiltration.postValue(FilterState.EmptyFilters)
        }
    }

    fun createFilterFromShared(): FilterSearch {
        val industryId = interactor.getFilter()?.industryId
        val onlyWithSalary = interactor.getFilter()?.isOnlyWithSalary
        val countryId = interactor.getFilter()?.countryId
        val regionId = interactor.getFilter()?.regionId
        val salary = interactor.getFilter()?.expectedSalary
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

    fun clearWorkplace() {
        interactor.clearCountry()
        interactor.clearArea()
        stateLiveDataArea.postValue(AreaState.EmptyWorkplace)
    }

    private fun setIndustry(industry: String) {
        stateLiveDataIndustry.postValue(IndustryState.FilterIndustryState(industry))
    }

    fun setIndustryIsEmpty() {
        interactor.clearIndustry()
        stateLiveDataIndustry.postValue(IndustryState.EmptyFilterIndustry)
    }

    fun setSalary(inputTextFromApply: String) {
        interactor.updateSalary(inputTextFromApply)
        stateLiveDataSalary.postValue(SalaryState.FilterSalaryState(inputTextFromApply))
    }

    fun setSalaryIsEmpty() {
        interactor.clearSalary()
        stateLiveDataSalary.postValue(SalaryState.EmptyFilterSalary)
    }

    fun setCheckboxOnlyWithSalary(checkbox: Boolean) {
        interactor.updateCheckBox(checkbox)
        stateLiveDataCheckBox.postValue(CheckBoxState.IsCheck(checkbox))
    }

    fun clearAllFilters() {
        clearWorkplace()
        setIndustryIsEmpty()
        setSalaryIsEmpty()
        setCheckboxOnlyWithSalary(false)
        stateLiveDataFiltration.postValue(FilterState.EmptyFilters)
    }

    fun setChangedState() {
        stateLiveDataFiltration.postValue(FilterState.ChangedFilter)
    }

    fun getIndustryFilterId(): String? {
        val filter = interactor.getFilter()
        val industryId = filter?.industryId
        return industryId
    }
}
