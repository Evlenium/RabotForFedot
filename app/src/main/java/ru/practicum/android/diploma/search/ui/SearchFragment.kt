package ru.practicum.android.diploma.search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.details.ui.VacancyDetailsFragment
import ru.practicum.android.diploma.search.domain.model.FilterSearch
import ru.practicum.android.diploma.search.domain.model.SimpleVacancy
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.search.presentation.VacanciesState
import ru.practicum.android.diploma.util.Constants

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!
    private var inputTextFromSearch: String? = null
    private var searchAdapter: SearchVacancyAdapter? = null
    private val viewModel by viewModel<SearchViewModel>()
    private var filterSearch: FilterSearch? = null
    private var doWeHaveToSearch = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.searchRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("key") { _, bundle ->
            doWeHaveToSearch = bundle.getBoolean(FLAG)

            val text = binding.textInputEditText.text.toString()
            val isNotEmpty = text.isNotBlank()

            if (isNotEmpty) {
                binding.textInputEndIcon.setImageResource(R.drawable.icon_close)
                binding.textInputEndIcon.isVisible = true

                if (doWeHaveToSearch) {
                    binding.placeholderViewGroup.isVisible = false
                    viewModel.downloadData(text)
                }
            }
        }

        filterSearch = viewModel.createFilterFromShared()
        viewModel.setFilterSearch(filterSearch)
        val isWorkplaceFilter = filterSearch?.countryId != null || filterSearch?.regionId != null
        val isIndustryFilter = filterSearch?.industryId != null
        val isSalaryFilter = filterSearch?.expectedSalary != null || filterSearch?.isOnlyWithSalary != false

        if (isWorkplaceFilter || isIndustryFilter || isSalaryFilter) {
            binding.filterButton.setImageResource(R.drawable.icon_filter_on)
        } else {
            binding.filterButton.setImageResource(R.drawable.icon_filter_off)
        }
        scrollListener()
        searchAdapterReset()
        setupToolbar()
        binding.placeholderViewGroup.animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding.placeholderViewGroup.animate()
        with(binding) {
            textInputEndIcon.setOnClickListener {
                textInputEditText.setText("")
                textInputEndIcon.setImageResource(R.drawable.icon_search)
                viewModel.lastText = ""
                activity?.window?.currentFocus?.let { view ->
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
                showPlaceholderSearch()
            }
        }
        inputEditTextInit()
        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        binding.filterButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_searchFragment_to_filterSettingsFragment
            )
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    private fun scrollListener() {
        binding.searchRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.searchRecyclerView.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount
                    val isLoadingNeeded = lastVisibleItemPosition + 1 == totalItemCount
                    if (isLoadingNeeded && viewModel.totalVacanciesCount > Constants.VACANCIES_PER_PAGE && dy > 0
                    ) {
                        viewModel.uploadData()
                    }
                }
            }
        })
    }

    private fun inputEditTextInit() {
        binding.textInputEditText.addTextChangedListener(
            beforeTextChanged = { s, start, count, after -> },
            onTextChanged = { s, start, before, count ->
                if (s != null) {
                    val stringIsNotEmpty = s.trim().isNotEmpty()
                    if (stringIsNotEmpty && viewModel.lastText != s.toString()) {
                        binding.textInputEndIcon.setImageResource(R.drawable.icon_close)
                        inputTextFromSearch = s.toString()
                        binding.vacancyMessageTextView.isVisible = false
                        binding.placeholderViewGroup.isVisible = false
                        searchAdapterReset()

                        viewModel.searchDebounce(inputTextFromSearch!!)
                    } else if (stringIsNotEmpty && viewModel.lastText.isEmpty()) {
                        binding.textInputEndIcon.setImageResource(R.drawable.icon_search)
                        showPlaceholderSearch()
                        binding.vacancyMessageTextView.visibility = View.GONE
                    }
                }
            },
            afterTextChanged = { s -> inputTextFromSearch = s.toString() }
        )

        binding.textInputEditText.setOnEditorActionListener { _, actionId, _ ->
            binding.centerProgressBar.visibility = View.GONE
            false
        }
    }

    private fun searchAdapterReset() {
        searchAdapter = null
        searchAdapter = SearchVacancyAdapter { vacancy ->
            if (clickDebounce()) {
                findNavController().navigate(
                    R.id.action_searchFragment_to_vacancyDetailsFragment,
                    VacancyDetailsFragment.createArgs(vacancy.id)
                )
            }
        }
        binding.searchRecyclerView.adapter = searchAdapter
    }

    private fun render(state: VacanciesState) {
        when (state) {
            is VacanciesState.Content -> {
                showContent(state.vacancies)
                val textCounter = requireContext().resources.getQuantityString(
                    R.plurals.plurals_vacancy,
                    viewModel.totalVacanciesCount,
                    viewModel.totalVacanciesCount
                )
                binding.vacancyMessageTextView.text = textCounter
                binding.vacancyMessageTextView.isVisible = true
            }

            is VacanciesState.Empty -> {
                showEmptyResult(state.message)
                binding.vacancyMessageTextView.isVisible = true
                binding.vacancyMessageTextView.text = getString(R.string.there_are_no_such_vacancies)
            }

            is VacanciesState.ErrorToast -> {
                Toast.makeText(requireContext(), getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show()
            }

            is VacanciesState.Error -> showErrorConnection(state.errorMessage)
            is VacanciesState.Loading -> showLoading()
            is VacanciesState.BottomLoading -> showBottomLoading()
        }
    }

    private fun showPlaceholderSearch() {
        with(binding) {
            centerProgressBar.isVisible = false
            vacancyMessageTextView.isVisible = false
            bottomProgressBar.isVisible = false
            placeholderViewGroup.isVisible = true
            placeholderTextView.isVisible = false
            searchRecyclerView.isVisible = false
            placeholderImageView.setImageResource(R.drawable.placeholder_empty_vacancy_search)
        }
    }

    private fun showLoading() {
        with(binding) {
            centerProgressBar.isVisible = true
            bottomProgressBar.isVisible = false
            placeholderViewGroup.isVisible = false
            searchRecyclerView.isVisible = false
            vacancyMessageTextView.isVisible = false
        }
    }

    private fun showBottomLoading() {
        with(binding) {
            centerProgressBar.isVisible = false
            bottomProgressBar.isVisible = true
            placeholderViewGroup.isVisible = false
            searchRecyclerView.isVisible = true
            vacancyMessageTextView.isVisible = true
        }
    }

    private fun showErrorConnection(errorMessage: String) {
        with(binding) {
            centerProgressBar.isVisible = false
            bottomProgressBar.isVisible = false
            placeholderViewGroup.isVisible = true
            searchRecyclerView.isVisible = false
            placeholderTextView.isVisible = true
            placeholderTextView.text = errorMessage
            vacancyMessageTextView.isVisible = false
            placeholderImageView.setImageResource(
                if (errorMessage == getString(R.string.no_internet)) {
                    R.drawable.placeholder_no_internet
                } else {
                    R.drawable.placeholder_error_server_vacancy_search
                }
            )
        }
    }

    private fun showEmptyResult(message: String) {
        with(binding) {
            centerProgressBar.isVisible = false
            bottomProgressBar.isVisible = false
            placeholderViewGroup.isVisible = true
            searchRecyclerView.isVisible = false
            placeholderTextView.isVisible = true
            placeholderTextView.text = message
            placeholderImageView.setImageResource(R.drawable.placeholder_incorrect_input_data)
        }
    }

    private fun showContent(vacancies: ArrayList<SimpleVacancy>) {
        with(mutableListOf<SimpleVacancy>()) {
            addAll(vacancies)
            searchAdapter?.addItemsInRecycler(vacancies)
        }
        with(binding) {
            centerProgressBar.isVisible = false
            bottomProgressBar.isVisible = false
            placeholderViewGroup.isVisible = false
            searchRecyclerView.isVisible = true
        }
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.searchToolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            title = getString(R.string.search_vacancies)
        }
        binding.searchToolbar.setTitleTextAppearance(requireContext(), R.style.ToolbarAppStyle)
    }

    private fun clickDebounce(): Boolean {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(CLICK_DEBOUNCE_DELAY)
        }
        return true
    }

    override fun onDestroyView() {
        searchAdapter = null
        binding.searchRecyclerView.adapter = null
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val FLAG = "flag"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
