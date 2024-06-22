package ru.practicum.android.diploma.filter.region.ui

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
import ru.practicum.android.diploma.databinding.FragmentFilterRegionBinding
import ru.practicum.android.diploma.filter.region.presentation.RegionViewModel
import ru.practicum.android.diploma.filter.region.presentation.model.RegionState
import ru.practicum.android.diploma.filter.workplace.ui.WorkplaceFragment
import ru.practicum.android.diploma.search.domain.model.Area
import java.util.Locale

class RegionFragment : Fragment() {
    private var _binding: FragmentFilterRegionBinding? = null
    private val binding: FragmentFilterRegionBinding get() = _binding!!
    private var regionAdapter: RegionAdapter? = null
    private var inputTextFromSearch: String? = null
    private var listRegion = emptyList<Area>()
    private val viewModel by viewModel<RegionViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFilterRegionBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            resetImageButton.setOnClickListener {
                hideEmptyPlaceholder()
                textInputEditText.setText("")
                regionAdapter?.setItems(listRegion)
                activity?.window?.currentFocus?.let { view ->
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
        regionAdapterInit()
        inputEditTextInit()

        val backPath = R.id.action_filterRegionFragment_to_filterWorkplaceFragment
        binding.buttonBack.setOnClickListener { findNavController().navigate(backPath) }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(backPath)
            }
        })

        viewModel.searchRequest()
        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
    }

    private fun render(state: RegionState) {
        when (state) {
            is RegionState.Content -> showContent(state.regions)
            is RegionState.Error -> showError(state.errorMessage)
            is RegionState.Loading -> showLoading()
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
        binding.placeholderMessage.text = requireContext().getString(R.string.there_is_no_such_region)
    }

    private fun hideEmptyPlaceholder() {
        binding.placeholderContainer.isVisible = false
        binding.placeholderImage.isVisible = false
        binding.placeholderMessage.isVisible = false
    }

    private fun showContent(regions: ArrayList<Area>) {
        with(binding) {
            progressBar.isVisible = false
            placeholderContainer.isVisible = false
            recyclerView.isVisible = true
        }

        listRegion = regions
        regionAdapter?.setItems(regions)
    }

    private fun inputEditTextInit() {
        binding.textInputEditText.addTextChangedListener(
            beforeTextChanged = { s, start, count, after -> },
            onTextChanged = { s, start, before, count ->
                binding.resetImageButton.visibility = clearButtonVisibility(s)
                if (s.isNullOrEmpty()) {
                    regionAdapter?.setItems(listRegion)
                }
                inputTextFromSearch = s.toString()
                containsMethod(inputTextFromSearch)
            },
            afterTextChanged = { s ->
                inputTextFromSearch = s.toString()
            }
        )
        binding.textInputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                containsMethod(inputTextFromSearch)
                true
            }
            binding.progressBar.visibility = View.GONE
            false
        }
    }

    private fun containsMethod(inputTextFromSearch: String?) {
        if (!viewModel.getInternetConnection()) {
            showErrorListDownload()
        } else if (!inputTextFromSearch.isNullOrEmpty()) {
            inputTextFromSearch.replaceFirstChar {
                if (it.isLowerCase()) {
                    it.titlecase(Locale.getDefault())
                } else {
                    it.toString()
                }
            }
            val filteredList = listRegion.filter { industry ->
                industry.name!!.lowercase().contains(inputTextFromSearch)
            }
            if (filteredList.isEmpty()) {
                showEmptyPlaceholder()
            } else {
                hideEmptyPlaceholder()
            }
            regionAdapter?.setItems(filteredList)
        } else {
            hideEmptyPlaceholder()
        }
    }

    private fun showErrorListDownload() {
        with(binding) {
            progressBar.isVisible = false
            recyclerView.isVisible = false
            placeholderContainer.isVisible = true
            placeholderImage.isVisible = true
            placeholderMessage.isVisible = true
            placeholderImage.setImageResource(R.drawable.placeholder_empty_region_list)
            placeholderMessage.text = requireContext().getString(R.string.failed_to_get_list)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun regionAdapterInit() {
        regionAdapter = null
        regionAdapter = RegionAdapter { region ->
            if (region.name != null) {
                viewModel.setCountryFilter(region)
                viewModel.setParentId(region.parentId)
                findNavController().navigate(
                    R.id.action_filterRegionFragment_to_filterWorkplaceFragment,
                    WorkplaceFragment.createArgs(viewModel.getRegionCountry())
                )
            }
        }
        binding.recyclerView.adapter = regionAdapter
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        regionAdapter = null
        _binding = null
        super.onDestroyView()
    }
}
