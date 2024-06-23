package ru.practicum.android.diploma.filter.filtration.presentation

sealed interface FullFilterState {
    object EmptyFilters : FullFilterState
    object NonEmptyFilters : FullFilterState
}

sealed interface ChangeFilterState {
    object ChangedFilter : ChangeFilterState
    object NoChangeFilters : ChangeFilterState
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
