package ru.practicum.android.diploma.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.search.domain.model.SimpleVacancy
import ru.practicum.android.diploma.util.Creator

class SearchVacancyViewHolder(
    private val binding: ItemVacancyBinding,
    onItemClick: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }

    fun bind(model: SimpleVacancy) {
        val stringMain: String = if (model.address == null) {
            model.name
        } else {
            "${model.name}, ${model.address}"
        }
        binding.vacancyTitle.text = stringMain
        binding.employer.text = model.employer!!.name
        binding.salary.text = Creator.getSalary(context = itemView.context, salary = model.salary)

        Glide.with(itemView)
            .load(model.employer.logoUrls)
            .placeholder(R.drawable.icon_android_placeholder)
            .centerCrop()
            .transform(RoundedCorners(Creator.dpToPx(RADIUS_IN_DP)))
            .into(binding.vacancyCover)
    }

    companion object {
        private const val RADIUS_IN_DP = 8
    }
}
