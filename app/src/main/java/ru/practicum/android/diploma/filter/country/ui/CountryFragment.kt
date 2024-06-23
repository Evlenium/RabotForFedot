package ru.practicum.android.diploma.filter.country.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterCountryBinding
import ru.practicum.android.diploma.filter.country.presentation.CountryViewModel
import ru.practicum.android.diploma.filter.country.presentation.model.CountryState
import ru.practicum.android.diploma.search.domain.model.Country

class CountryFragment : Fragment() {
    private var _binding: FragmentFilterCountryBinding? = null
    private val binding: FragmentFilterCountryBinding get() = _binding!!

    private var countryAdapter: CountryAdapter? = null

    private val viewModel by viewModel<CountryViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilterCountryBinding.inflate(inflater, container, false)
        binding.countryRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countryAdapterInit()

        val backPath = R.id.action_filterCountryFragment_to_filterWorkplaceFragment
        binding.buttonBack.setOnClickListener { findNavController().navigate(backPath) }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(backPath)
            }
        })

        viewModel.searchRequest()
        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
    }

    private fun render(state: CountryState) {
        when (state) {
            is CountryState.Content -> showContent(state.countries)
            is CountryState.Error -> showError(state.errorMessage)
            is CountryState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.isVisible = true
            placeholderContainer.isVisible = false
            countryRecyclerView.isVisible = false
        }
    }

    private fun showError(errorMessage: String) {
        with(binding) {
            progressBar.isVisible = false
            placeholderContainer.isVisible = true
            countryRecyclerView.isVisible = false
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

    private fun showContent(countries: ArrayList<Country>) {
        with(binding) {
            progressBar.isVisible = false
            placeholderContainer.isVisible = false
            countryRecyclerView.isVisible = true
        }
        with(mutableListOf<Country>()) {
            addAll(countries)
            countryAdapter?.setItems(countries)
        }
    }

    private fun countryAdapterInit() {
        countryAdapter = null
        countryAdapter = CountryAdapter { country ->
            if (country.name != null) {
                viewModel.setCountryFilter(country)
                findNavController().navigate(
                    R.id.action_filterCountryFragment_to_filterWorkplaceFragment,
                )
            }
        }
        binding.countryRecyclerView.adapter = countryAdapter
    }

    override fun onDestroyView() {
        binding.countryRecyclerView.adapter = null
        countryAdapter = null
        _binding = null
        super.onDestroyView()
    }
}
