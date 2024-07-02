package ru.practicum.android.diploma.filter.industry.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.search.domain.model.Industry

class FilterIndustryAdapter(
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<FilterIndustryViewHolder>() {

    private var industries: MutableList<Industry> = mutableListOf()
    private var selectedPosition = RecyclerView.NO_POSITION
    private var selectedIndustryId: String? = null

    fun setPosition(position: Int) {
        selectedPosition = position
        selectedIndustryId = industries.getOrNull(position)?.id
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Industry>) {
        industries.clear()
        industries.addAll(items)
        selectedPosition = industries.indexOfFirst { it.id == selectedIndustryId }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterIndustryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FilterIndustryViewHolder(
            ItemIndustryBinding.inflate(layoutInflater, parent, false)
        ) { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                val previousPosition = selectedPosition
                selectedPosition = position
                selectedIndustryId = industries.getOrNull(position)?.id
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                industries.getOrNull(position)?.let { industry ->
                    itemClickListener.onItemClick(industry)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: FilterIndustryViewHolder, position: Int) {
        industries.getOrNull(position)?.let { industry ->
            holder.bind(industry, position == selectedPosition)
        }
    }

    override fun getItemCount() = industries.size

    fun interface ItemClickListener {
        fun onItemClick(industry: Industry)
    }
}
