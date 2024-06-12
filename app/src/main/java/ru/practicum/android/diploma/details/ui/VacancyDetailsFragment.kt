package ru.practicum.android.diploma.details.ui

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.details.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.details.presentation.model.StateLoadVacancy
import ru.practicum.android.diploma.search.domain.model.Contacts
import ru.practicum.android.diploma.search.domain.model.Phone
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.Creator

class VacancyDetailsFragment : Fragment() {
    private var _binding: FragmentVacancyDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<VacancyDetailsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVacancyDetailsBinding.inflate(inflater, container, false)
        setupToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vacancyId = requireArguments().getString(ARGS_VACANCY_ID)
        if (vacancyId != null) { viewModel.searchRequest(vacancyId) }
        viewModel.observeVacancy().observe(viewLifecycleOwner) { render(it) }
        binding.favoriteButton.setOnClickListener { viewModel.onFavoriteClicked() }
    }

    private fun updateFavoriteFlag(isFavorite: Boolean): Int {
        return if (isFavorite) {
            R.drawable.icon_favorite_on
        } else {
            R.drawable.icon_favorite_off
        }
    }

    private fun showContent(vacancy: Vacancy) {
        with(binding) {
            progressBar.isVisible = false
            vacancyDetailsNestedScrollView.isVisible = true
            placeholderContainer.isVisible = false
            shareButton.setOnClickListener { viewModel.shareVacancy(vacancy.alternateUrl.toString()) }
            vacancyNameTextView.text = vacancy.name
            salaryTextView.text = Creator.getSalary(requireContext(), vacancy.salary)
            Glide.with(employerLogo)
                .load(vacancy.employer?.logoUrls)
                .placeholder(R.drawable.icon_android_placeholder)
                .centerCrop()
                .transform(RoundedCorners(Creator.dpToPx(RADIUS_IN_DP)))
                .into(employerLogo)
            employerNameTextView.text = vacancy.employer?.name
            employerCityTextView.text = vacancy.address ?: vacancy.area
            experienceTextView.text = vacancy.experience
            employmentAndScheduleTextView.text = vacancy.schedule
            if (vacancy.description != null) {
                vacancyDescriptionValueTextView.setText(
                    Html.fromHtml(
                        vacancy.description,
                        Html.FROM_HTML_MODE_COMPACT
                    )
                )
            } else {
                sectionVacancyDescription.isVisible = false
            }
            if (vacancy.keySkills.isNullOrEmpty()) {
                sectionKeySkills.isVisible = false
                keySkillsValueTextView.isVisible = false
            } else {
                keySkillsValueTextView.text = getKeySkills(vacancy.keySkills)
            }
            showVacancyContacts(vacancy.contacts)
        }
    }

    private fun showVacancyContacts(contacts: Contacts?) {
        with(binding) {
            if (contacts?.name.isNullOrEmpty() &&
                contacts?.email.isNullOrEmpty() && contacts?.phones.isNullOrEmpty()
            ) {
                contactsContainer.isVisible = false
            } else {
                contactsContainer.isVisible = true
                contactPersonValueTextView.text = contacts?.name
                if (!contacts?.email.isNullOrEmpty()) {
                    emailValueTextView.isClickable = true
                    emailValueTextView.isVisible = true
                    emailValueTextView.text = contacts?.email
                    emailValueTextView.setOnClickListener {
                        contacts?.email?.let { mail -> viewModel.writeToEmployer(mail) }
                    }
                }
                if (!contacts?.phones.isNullOrEmpty()) {
                    phoneValueTextView.isClickable = true
                    phoneValueTextView.isVisible = true
                    phoneValueTextView.text = getPhones(contacts?.phones)
                    phoneValueTextView.setOnClickListener {
                        viewModel.callPhoneNumber(getPhones(contacts?.phones))
                    }
                }
                commentValueTextView.text = getComments(contacts?.phones)
            }
        }
    }

    private fun getComments(phones: List<Phone>?): String {
        var resultString = ""
        phones?.forEach { phone ->
            resultString += "${phone.comment} \n"
        }
        return resultString
    }

    private fun getPhones(phones: List<Phone>?): String {
        var resultString = ""
        phones?.forEach { phone ->
            resultString += "${phone.country} ${phone.city} " +
                "${phone.number} \n"
        }
        return resultString
    }

    private fun getKeySkills(keySkills: List<String?>?): String {
        var resultString = ""
        if (keySkills.isNullOrEmpty()) {
            binding.sectionKeySkills.isVisible = false
        } else {
            binding.sectionKeySkills.isVisible = true
            keySkills.forEach { skill ->
                resultString += "$skill\n"
            }
        }
        return resultString
    }

    private fun render(state: StateLoadVacancy) {
        when (state) {
            is StateLoadVacancy.Content -> {
                showContent(state.data)
                binding.favoriteButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        updateFavoriteFlag(state.isFavorite),
                        null
                    )
                )
            }

            is StateLoadVacancy.Error -> {
                showError(state.errorMessage)
            }

            is StateLoadVacancy.Loading -> {
                showProgressBar()
            }
        }
    }

    private fun showProgressBar() {
        with(binding) {
            progressBar.isVisible = true
            vacancyDetailsNestedScrollView.isVisible = false
            placeholderContainer.isVisible = false
        }
    }

    private fun showError(message: String) {
        with(binding) {
            progressBar.isVisible = false
            vacancyDetailsNestedScrollView.isVisible = false
            placeholderContainer.isVisible = true
            placeholderMessage.text = message
            placeholderImage.setImageResource(R.drawable.placeholder_error_server_vacancy_search)
        }
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.detailsVacancyToolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.icon_back)
            title = getString(R.string.job_vacancy)
        }

        binding.detailsVacancyToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARGS_VACANCY_ID = "vacancy_id"
        private const val RADIUS_IN_DP = 8
        fun createArgs(vacancyId: String): Bundle =
            bundleOf(
                ARGS_VACANCY_ID to vacancyId,
            )
    }
}
