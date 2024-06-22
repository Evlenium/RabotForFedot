package ru.practicum.android.diploma.filter.filtration.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.filter.filtration.presentation.AreaState
import ru.practicum.android.diploma.filter.filtration.presentation.ChangeFilterState
import ru.practicum.android.diploma.filter.filtration.presentation.CheckBoxState
import ru.practicum.android.diploma.filter.filtration.presentation.FilterSettingsViewModel
import ru.practicum.android.diploma.filter.filtration.presentation.FullFilterState
import ru.practicum.android.diploma.filter.filtration.presentation.IndustryState
import ru.practicum.android.diploma.filter.industry.ui.FilterIndustryFragment
import ru.practicum.android.diploma.search.ui.SearchFragment

class FilterSettingsFragment : Fragment() {
    private var _binding: FragmentFilterSettingsBinding? = null
    private val binding get() = _binding!!

    private var inputTextFromApply: String? = null
    private val viewModel by viewModel<FilterSettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFilterSettingsBinding.inflate(inflater, container, false)
        setupToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val fromRegion = arguments?.getBoolean(ARGS_FROM_WORKPLACE)
            val fromIndustry = arguments?.getBoolean(ARGS_FROM_INDUSTRY)
            if (fromRegion == true || fromIndustry == true) {
                viewModel.setChangedState()
            }
        }
        setOnClickListeners()
        setOnTextChangedListener()
        setOnFocusChangeListener()
        if (viewModel.salary != null) {
            binding.salaryEditText.setText(viewModel.salary.toString(), TextView.BufferType.EDITABLE)
        }
        viewModel.updateFilterParametersFromShared()
        viewModel.observeAreaState().observe(viewLifecycleOwner) { renderArea(it) }
        viewModel.observeIndustryState().observe(viewLifecycleOwner) { renderIndustry(it) }
        viewModel.observeCheckboxState().observe(viewLifecycleOwner) { renderCheckBox(it) }
        viewModel.observeFiltrationState().observe(viewLifecycleOwner) { renderFullState(it) }
        viewModel.observeFiltrationStateChanged().observe(viewLifecycleOwner) { renderChangedState(it) }
    }

    private fun renderChangedState(filterState: ChangeFilterState) {
        when (filterState) {
            is ChangeFilterState.ChangedFilter -> binding.applyFilterButton.isVisible = true
            is ChangeFilterState.NoChangeFilters -> binding.applyFilterButton.isVisible = false
        }
    }

    private fun renderIndustry(state: IndustryState) {
        when (state) {
            is IndustryState.FilterIndustryState -> showIndustry(state.industryName)
            is IndustryState.EmptyFilterIndustry -> setDefaultIndustry()
        }
    }

    private fun renderCheckBox(checkBoxState: CheckBoxState) {
        when (checkBoxState) {
            is CheckBoxState.IsCheck -> setCheckBox(checkBoxState.isCheck)
        }
    }

    private fun renderArea(state: AreaState) {
        when (state) {
            is AreaState.WorkPlaceState -> showWorkPlace(state.area)
            is AreaState.EmptyWorkplace -> setDefaultWorkplace()
        }
    }

    private fun renderFullState(state: FullFilterState) {
        when (state) {
            is FullFilterState.EmptyFilters -> binding.resetFilterButton.isVisible = false
            is FullFilterState.NonEmptyFilters -> binding.resetFilterButton.isVisible = true
        }
    }

    private fun showWorkPlace(workPlace: String) {
        binding.workPlaceHeader.isVisible = true
        binding.filtrationWorkPlaceImageView.setImageResource(R.drawable.icon_reset)
        binding.filtrationWorkPlaceTextView.setTextColor(
            requireContext().getColor(R.color.black_white_text_color_selector)
        )
        binding.filtrationWorkPlaceImageView.setOnClickListener {
            viewModel.setChangedState()
            viewModel.clearWorkplace()
            viewModel.checkFilters()
        }
        binding.filtrationWorkPlaceTextView.text = workPlace
        binding.resetFilterButton.isVisible = true
    }

    private fun setDefaultWorkplace() {
        binding.workPlaceHeader.isVisible = false
        binding.filtrationWorkPlaceTextView.text = getString(R.string.place_of_work)
        binding.filtrationWorkPlaceTextView.setTextColor(requireContext().getColor(R.color.gray))
        binding.filtrationWorkPlaceImageView.setImageResource(R.drawable.icon_arrow_forward)
        binding.filtrationWorkPlaceImageView.setOnClickListener(null)
    }

    private fun showIndustry(industryName: String) {
        binding.industryHeader.isVisible = true
        binding.filtrationIndustryImageView.setImageResource(R.drawable.icon_reset)
        binding.filtrationIndustryTextView.setTextColor(
            requireContext().getColor(R.color.black_white_text_color_selector)
        )
        binding.filtrationIndustryImageView.setOnClickListener {
            viewModel.setChangedState()
            viewModel.setIndustryIsEmpty()
            viewModel.checkFilters()
        }
        binding.filtrationIndustryTextView.text = industryName
        binding.resetFilterButton.isVisible = true
    }

    private fun setDefaultIndustry() {
        binding.industryHeader.isVisible = false
        binding.filtrationIndustryTextView.text = getString(R.string.industry)
        binding.filtrationIndustryTextView.setTextColor(requireContext().getColor(R.color.gray))
        binding.filtrationIndustryImageView.setImageResource(R.drawable.icon_arrow_forward)
        binding.filtrationIndustryImageView.setOnClickListener(null)
    }

    private fun setCheckBox(check: Boolean) {
        val lastState = viewModel.getFilter()?.isOnlyWithSalary
        if (lastState != null && check != lastState) {
            renderChangedState(ChangeFilterState.ChangedFilter)
        } else if (viewModel.stateLiveDataFiltration.value == FullFilterState.EmptyFilters ||
            viewModel.stateLiveDataChanged.value == null
        ) {
            renderChangedState(ChangeFilterState.NoChangeFilters)
        }
        if (check) {
            binding.filtrationPayCheckbox.isChecked = true
            renderFullState(FullFilterState.NonEmptyFilters)
        } else if (viewModel.stateLiveDataFiltration.value == FullFilterState.EmptyFilters) {
            renderFullState(FullFilterState.EmptyFilters)
        }
        viewModel.checkFilters()
    }

    private fun setOnClickListeners() {
        with(binding) {
            filtrationWorkPlace.setOnClickListener {
                findNavController().navigate(R.id.action_filterSettingsFragment_to_filterWorkplaceFragment)
            }
            filtrationIndustry.setOnClickListener {
                val arguments = FilterIndustryFragment.createBundle(viewModel.getIndustryFilterId())
                findNavController().navigate(
                    R.id.action_filterSettingsFragment_to_filterIndustryFragment,
                    arguments
                )
            }
            filtrationPayCheckbox.setOnClickListener {
                viewModel.stateLiveDataCheckBox.postValue(CheckBoxState.IsCheck(filtrationPayCheckbox.isChecked))
            }
            applyFilterButton.setOnClickListener {
                viewModel.setCheckboxOnlyWithSalary(filtrationPayCheckbox.isChecked)
                findNavController().navigate(
                    R.id.action_filterSettingsFragment_to_searchFragment,
                    SearchFragment.createArgsFilter(viewModel.createFilterFromShared())
                )
            }

            resetSalaryButton.setOnClickListener {
                salaryEditText.setText("")
            }

            resetFilterButton.setOnClickListener {
                viewModel.clearAllFilters()
                viewModel.setSalaryIsEmpty()
                filtrationPayCheckbox.isChecked = false
                binding.salaryEditText.setText("")
                binding.resetFilterButton.isVisible = false
                viewModel.setNoChangedState()
            }
        }
    }

    private fun setOnFocusChangeListener() {
        binding.salaryEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!binding.salaryEditText.text.isNullOrEmpty()) {
                binding.resetSalaryButton.isVisible = hasFocus
            }

            if (hasFocus) {
                val textColorHint = ContextCompat.getColor(requireContext(), R.color.blue)
                binding.salaryDescriptionField.setDefaultHintTextColor(ColorStateList.valueOf(textColorHint))
            }
        }
    }

    private fun setOnTextChangedListener() {
        binding.salaryEditText.addTextChangedListener(
            beforeTextChanged = { s, start, count, after -> },
            onTextChanged = { s, start, before, count -> },
            afterTextChanged = { s ->
                if (!s.isNullOrEmpty()) {
                    if (!binding.salaryEditText.hasFocus()) {
                        val textColorHint = ContextCompat.getColor(requireContext(), R.color.black)
                        binding.salaryDescriptionField.setDefaultHintTextColor(ColorStateList.valueOf(textColorHint))
                    } else {
                        val textColorHint = ContextCompat.getColor(requireContext(), R.color.blue)
                        binding.salaryDescriptionField.setDefaultHintTextColor(ColorStateList.valueOf(textColorHint))
                    }
                    binding.resetSalaryButton.isVisible = binding.salaryEditText.hasFocus()
                    inputTextFromApply = s.toString()
                    viewModel.setSalary(inputTextFromApply!!)
                    if (viewModel.salary.toString().isNotEmpty() && viewModel.salary.toString() != inputTextFromApply) {
                        viewModel.setChangedState()
                    }
                    binding.resetFilterButton.isVisible = true
                } else {
                    val textColorHint = ContextCompat.getColor(requireContext(), R.color.gray_white_text_color_selector)
                    binding.salaryDescriptionField.setDefaultHintTextColor(ColorStateList.valueOf(textColorHint))
                    binding.resetSalaryButton.isVisible = false
                    viewModel.setSalaryIsEmpty()
                }
                viewModel.checkFilters()
            }
        )
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.filtrationVacancyToolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.icon_back)
        }

        val backPath = R.id.action_filterSettingsFragment_to_searchFragment
        binding.filtrationVacancyToolbar.setTitleTextAppearance(requireContext(), R.style.ToolbarAppStyle)
        binding.filtrationVacancyToolbar.setNavigationOnClickListener {
            viewModel.clearAllFilters()
            findNavController().navigate(backPath)
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.clearAllFilters()
                findNavController().navigate(backPath)
            }
        })
    }

    companion object {
        private const val ARGS_FROM_WORKPLACE = "from_workplace"
        fun createArgsFromWorkplace(isFromWorkplace: Boolean): Bundle =
            bundleOf(
                ARGS_FROM_WORKPLACE to isFromWorkplace,
            )

        private const val ARGS_FROM_INDUSTRY = "from_industry"
        fun createArgsFromIndustry(isFromIndustry: Boolean): Bundle =
            bundleOf(
                ARGS_FROM_INDUSTRY to isFromIndustry,
            )
    }
}
