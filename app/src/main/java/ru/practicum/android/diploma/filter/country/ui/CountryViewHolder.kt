package ru.practicum.android.diploma.filter.country.ui

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.search.domain.model.Country

class CountryViewHolder(
    private val binding: ItemCountryBinding,
    onItemClick: (position: Int) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }

    fun bind(model: Country) {
        binding.filterCountryItem.text = model.name
    }
}
