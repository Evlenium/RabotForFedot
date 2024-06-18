package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterWorkplaceBinding

class FilterWorkplaceFragment : Fragment() {
    private var _binding: FragmentFilterWorkplaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFilterWorkplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBack.setOnClickListener { parentFragmentManager.popBackStack() }

        binding.selectButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_filterWorkplaceFragment_to_filterSettingsFragment,
                FilterSettingsFragment.createArgsFromWorkplace(true)
            )
        }

        val backPath = R.id.action_filterWorkplaceFragment_to_filterSettingsFragment
        binding.buttonBack.setOnClickListener { findNavController().navigate(backPath) }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { findNavController().navigate(backPath) }
        })
    }
}
