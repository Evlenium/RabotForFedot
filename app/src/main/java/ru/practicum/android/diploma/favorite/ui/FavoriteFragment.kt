package ru.practicum.android.diploma.favorite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoriteBinding
import ru.practicum.android.diploma.details.ui.VacancyDetailsFragment
import ru.practicum.android.diploma.favorite.presentation.FavoriteScreenState
import ru.practicum.android.diploma.favorite.presentation.FavoriteViewModel
import ru.practicum.android.diploma.search.domain.model.SimpleVacancy
import ru.practicum.android.diploma.search.ui.SearchVacancyAdapter

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavoriteViewModel>()
    private var adapter: SearchVacancyAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        view.animate()

        binding.favoritesToolbar.setTitleTextAppearance(requireContext(), R.style.ToolbarAppStyle)
        adapter = SearchVacancyAdapter { vacancy -> onClickDebounce(vacancy) }
        binding.recyclerView.adapter = adapter

        viewModel.updateData()
        viewModel.observeScreenState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoriteScreenState.Loading -> {
                    showLoading()
                }

                is FavoriteScreenState.Content -> {
                    showContent(state.data)
                }

                is FavoriteScreenState.Empty -> {
                    showEmpty()
                }

                is FavoriteScreenState.Error -> {
                    showError()
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        adapter = null
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.recyclerView.isVisible = false
        binding.placeholderContainer.isVisible = false
    }

    private fun showContent(data: List<SimpleVacancy>) {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = true
        adapter?.setItems(data)
        binding.placeholderContainer.isVisible = false
    }

    private fun showEmpty() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.placeholderContainer.isVisible = true
        binding.placeholderMessage.text = requireContext().getString(R.string.the_list_is_empty)
        binding.placeholderImage.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.placeholder_empty_favorite_list,
                null
            )
        )
    }

    private fun showError() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.placeholderContainer.isVisible = true
        binding.placeholderMessage.text = requireContext().getString(R.string.failed_to_get_list_of_vacancies)
        binding.placeholderImage.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.placeholder_incorrect_input_data,
                null
            )
        )
    }

    private var isClickAllowed = true

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun onClickDebounce(vacancy: SimpleVacancy) {
        if (clickDebounce()) {
            val arguments = VacancyDetailsFragment.createArgs(vacancy.id)
            findNavController().navigate(
                R.id.action_favoriteFragment_to_vacancyDetailsFragment,
                arguments
            )
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
