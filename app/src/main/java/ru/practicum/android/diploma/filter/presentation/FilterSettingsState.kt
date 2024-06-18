package ru.practicum.android.diploma.filter.presentation

sealed interface FilterState {
    object EmptyFilters : FilterState
    object ChangedFilter : FilterState
}

sealed interface AreaState {
    object EmptyWorkplace : AreaState
    data class WorkPlaceState(val area: String) : AreaState
}

sealed interface SalaryState {
    object EmptyFilterSalary : SalaryState
    data class FilterSalaryState(val salary: String) : SalaryState
}

sealed interface IndustryState {
    object EmptyFilterIndustry : IndustryState
    data class FilterIndustryState(val industryName: String) : IndustryState
}

sealed interface CheckBoxState {
    data class IsCheck(val isCheck: Boolean) : CheckBoxState
}
