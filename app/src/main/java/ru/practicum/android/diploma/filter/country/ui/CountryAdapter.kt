package ru.practicum.android.diploma.filter.country.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.search.domain.model.Country

class CountryAdapter(
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<CountryViewHolder>() {

    private val areas: MutableList<Country> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Country>) {
        areas.clear()
        areas.addAll(items.toMutableList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return CountryViewHolder(ItemCountryBinding.inflate(layoutInspector, parent, false)) { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                areas.getOrNull(position)?.let { area ->
                    itemClickListener.onItemClick(area)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        areas.getOrNull(position)?.let { holder.bind(it) }
    }

    override fun getItemCount() = areas.size

    fun interface ItemClickListener {
        fun onItemClick(country: Country)
    }
}
