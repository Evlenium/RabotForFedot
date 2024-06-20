package ru.practicum.android.diploma.filter.region.ui

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.search.domain.model.Area

class RegionViewHolder(
    private val binding: ItemCountryBinding,
    onItemClick: (position: Int) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }

    fun bind(model: Area) {
        binding.filterCountryItem.text = model.name
    }
}
