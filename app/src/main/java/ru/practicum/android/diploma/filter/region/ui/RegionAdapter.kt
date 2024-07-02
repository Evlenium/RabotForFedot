package ru.practicum.android.diploma.filter.region.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.search.domain.model.Area

class RegionAdapter(
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<RegionViewHolder>() {

    private val areas: MutableList<Area> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Area>) {
        areas.clear()
        areas.addAll(items.toMutableList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return RegionViewHolder(ItemCountryBinding.inflate(layoutInspector, parent, false)) { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                areas.getOrNull(position)?.let { area ->
                    itemClickListener.onItemClick(area)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        areas.getOrNull(position)?.let { holder.bind(it) }
    }

    override fun getItemCount() = areas.size

    fun interface ItemClickListener {
        fun onItemClick(country: Area)
    }
}
