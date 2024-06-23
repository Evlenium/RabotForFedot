package ru.practicum.android.diploma.filter.workplace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterWorkplaceBinding
import ru.practicum.android.diploma.filter.filtration.ui.FilterSettingsFragment
import ru.practicum.android.diploma.filter.workplace.presentation.WorkplaceViewModel
import ru.practicum.android.diploma.filter.workplace.presentation.model.AreaState

class WorkplaceFragment : Fragment() {
    private var _binding: FragmentFilterWorkplaceBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<WorkplaceViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilterWorkplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val countryName = arguments?.getString(ARGS_COUNTRY_NAME)
            if (countryName != null) {
                viewModel.setArgumentCountry(countryName)
            }
        }

        val backPath = R.id.action_filterWorkplaceFragment_to_filterSettingsFragment
        binding.buttonBack.setOnClickListener {
            viewModel.cleanRegionData()
            viewModel.cleanCountryData()
            findNavController().navigate(backPath)
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.cleanRegionData()
                viewModel.cleanCountryData()
                findNavController().navigate(backPath)
            }
        })

        viewModel.updateInfoFromShared()
        binding.resetCountryButton.setOnClickListener {
            viewModel.cleanCountryData()
        }
        binding.resetRegionButton.setOnClickListener {
            viewModel.cleanRegionData()
        }
        binding.selectButton.setOnClickListener {
            val areItemsTheSame = viewModel.isTheSameWorkplace()
            viewModel.updateFilterShared()
            findNavController().navigate(
                R.id.action_filterWorkplaceFragment_to_filterSettingsFragment,
                FilterSettingsFragment.createArgsFromWorkplace(!areItemsTheSame)
            )
        }
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: AreaState) {
        when (state) {
            is AreaState.Empty -> setDefaultScreenState()
            is AreaState.CountryName -> setCountryNameScreenState(state.country)
            is AreaState.RegionName -> setRegionNameScreenState(state.region)
            is AreaState.FullArea -> setFullAreaScreenState(state.country, state.region)
        }
    }

    private fun setFullAreaScreenState(countryName: String, regionName: String) {
        binding.apply {
            selectButton.isVisible = true
            resetRegionButton.isVisible = true
            filtrationRegionUnselected.isVisible = false
            filtrationRegionSelected.isVisible = true
            filtrationRegionName.text = regionName
            filtrationRegionSelected.setOnClickListener {
                findNavController().navigate(R.id.action_filterWorkplaceFragment_to_filterRegionFragment)
            }
            resetCountryButton.isVisible = true
            filtrationCountryUnselected.isVisible = false
            filtrationCountySelected.isVisible = true
            filtrationCountryName.text = countryName
            filtrationCountySelected.setOnClickListener {
                findNavController().navigate(R.id.action_filterWorkplaceFragment_to_filterCountryFragment)
            }
        }
    }

    private fun setRegionNameScreenState(regionName: String) {
        setDefaultScreenState()
        binding.apply {
            selectButton.isVisible = true
            resetRegionButton.isVisible = true
            filtrationRegionUnselected.isVisible = false
            filtrationRegionSelected.isVisible = true
            filtrationRegionName.text = regionName
            filtrationRegionSelected.setOnClickListener {
                findNavController().navigate(R.id.action_filterWorkplaceFragment_to_filterRegionFragment)
            }
        }
    }

    private fun setCountryNameScreenState(countryName: String) {
        setDefaultScreenState()
        binding.apply {
            selectButton.isVisible = true
            resetCountryButton.isVisible = true
            filtrationCountryUnselected.isVisible = false
            filtrationCountySelected.isVisible = true
            filtrationCountryName.text = countryName
            filtrationCountySelected.setOnClickListener {
                findNavController().navigate(R.id.action_filterWorkplaceFragment_to_filterCountryFragment)
            }
        }
    }

    private fun setDefaultScreenState() {
        with(binding) {
            selectButton.isVisible = false
            resetRegionButton.isVisible = false
            resetCountryButton.isVisible = false
            filtrationCountryUnselected.isVisible = true
            filtrationCountySelected.isVisible = false
            filtrationRegionUnselected.isVisible = true
            filtrationRegionSelected.isVisible = false
            filtrationCountryUnselected.setOnClickListener {
                findNavController().navigate(R.id.action_filterWorkplaceFragment_to_filterCountryFragment)
            }
            filtrationRegionUnselected.setOnClickListener {
                findNavController().navigate(R.id.action_filterWorkplaceFragment_to_filterRegionFragment)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARGS_COUNTRY_NAME = "country_name"
        fun createArgs(countryName: String?): Bundle =
            bundleOf(
                ARGS_COUNTRY_NAME to countryName,
            )
    }
}
