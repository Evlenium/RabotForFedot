package ru.practicum.android.diploma.filter.ui.industry

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding
import ru.practicum.android.diploma.filter.presentation.industry.FilterIndustryViewModel
import ru.practicum.android.diploma.filter.presentation.industry.IndustriesState
import ru.practicum.android.diploma.filter.presentation.industry.IndustryState
import ru.practicum.android.diploma.filter.ui.FilterSettingsFragment
import ru.practicum.android.diploma.search.domain.model.Industry
import java.util.Locale

class FilterIndustryFragment : Fragment() {
    private var _binding: FragmentFilterIndustryBinding? = null
    private val binding get() = _binding!!

    private var inputTextFromSearch: String? = null
    private var industryAdapter: FilterIndustryAdapter? = null
    private var listIndustries = emptyList<Industry>()
    private var industryId: String? = null

    private val viewModel by viewModel<FilterIndustryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        industryId = arguments?.getString(INDUSTRY_ID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFilterIndustryBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resetImageButton.setOnClickListener {
            hideEmptyPlaceholder()
            binding.textInputEditText.setText("")
            industryAdapter?.setItems(listIndustries)
            activity?.window?.currentFocus?.let { view ->
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        binding.selectButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_filterIndustryFragment_to_filterSettingsFragment,
                FilterSettingsFragment.createArgsFromIndustry(true)
            )
        }

        val backPath = R.id.action_filterIndustryFragment_to_filterSettingsFragment
        binding.buttonBack.setOnClickListener { findNavController().navigate(backPath) }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { findNavController().navigate(backPath) }
        })

        industryAdapter = FilterIndustryAdapter { viewModel.saveIndustryFromAdapter(it) }
        binding.recyclerView.adapter = industryAdapter
        inputEditTextInit()
        viewModel.searchRequest()
        viewModel.observeStateIndustry().observe(viewLifecycleOwner) { renderIndustry(it) }
        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
    }

    private fun renderIndustry(state: IndustryState) {
        when (state) {
            is IndustryState.ContentIndustry -> {
                binding.selectButton.isVisible = true
                viewModel.saveIndustry(state.industry)
            }

            is IndustryState.Empty -> binding.selectButton.isVisible = false
        }
    }

    private fun render(industriesState: IndustriesState) {
        when (industriesState) {
            is IndustriesState.Content -> showContent(industriesState.industries)
            is IndustriesState.Error -> showError(industriesState.errorMessage)
            is IndustriesState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.isVisible = true
            placeholderContainer.isVisible = false
            recyclerView.isVisible = false
        }
    }

    private fun showError(errorMessage: String) {
        with(binding) {
            progressBar.isVisible = false
            placeholderContainer.isVisible = true
            recyclerView.isVisible = false
            placeholderMessage.isVisible = true
            placeholderImage.isVisible = true
            placeholderMessage.text = errorMessage
            if (errorMessage == requireContext().getString(R.string.no_internet)) {
                placeholderImage.setImageResource(R.drawable.placeholder_no_internet)
            } else {
                placeholderImage.setImageResource(R.drawable.placeholder_error_server_vacancy_search)
            }
        }
    }

    private fun showEmptyPlaceholder() {
        binding.placeholderContainer.isVisible = true
        binding.placeholderImage.isVisible = true
        binding.placeholderMessage.isVisible = true
        binding.placeholderImage.setImageResource(R.drawable.placeholder_incorrect_input_data)
        binding.placeholderMessage.text = requireContext().getString(R.string.there_is_no_such_industry)
    }
    private fun hideEmptyPlaceholder() {
        binding.placeholderContainer.isVisible = false
        binding.placeholderImage.isVisible = false
        binding.placeholderMessage.isVisible = false
    }

    private fun showContent(industries: ArrayList<Industry>) {
        with(binding) {
            progressBar.isVisible = false
            placeholderContainer.isVisible = false
            recyclerView.isVisible = true
        }

        if (!industryId.isNullOrEmpty()) {
            industries.forEachIndexed { index, industry ->
                if (industry.id == industryId) {
                    industryAdapter?.setPosition(index)
                }
            }
        }

        listIndustries = industries
        industryAdapter?.setItems(listIndustries)
    }

    private fun inputEditTextInit() {
        binding.textInputEditText.addTextChangedListener(
            beforeTextChanged = { s, start, count, after -> },
            onTextChanged = { s, start, before, count ->
                binding.resetImageButton.visibility = clearButtonVisibility(s)
                if (!s.isNullOrEmpty()) {
                    inputTextFromSearch = s.toString()
                    containsMethod(inputTextFromSearch)
                }
            },
            afterTextChanged = { s ->
                inputTextFromSearch = s.toString()
            }
        )
        binding.textInputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.textInputEditText.text.toString().isNotEmpty()) {
                inputTextFromSearch?.let {
                }
                true
            }
            binding.progressBar.visibility = View.GONE
            false
        }
    }

    private fun containsMethod(inputTextFromSearch: String?) {
        if (!inputTextFromSearch.isNullOrEmpty()) {
            inputTextFromSearch.replaceFirstChar {
                if (it.isLowerCase()) {
                    it.titlecase(Locale.getDefault())
                } else {
                    it.toString()
                }
            }

            val filteredList = listIndustries.filter { industry ->
                industry.name.lowercase().contains(inputTextFromSearch)
            }

            if (filteredList.isEmpty()) {
                showEmptyPlaceholder()
            } else {
                hideEmptyPlaceholder()
            }

            industryAdapter?.setItems(filteredList)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        industryAdapter = null
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val INDUSTRY_ID = "INDUSTRY_ID"

        fun createBundle(industryId: String?) = Bundle().apply {
            putString(INDUSTRY_ID, industryId)
        }
    }
}
