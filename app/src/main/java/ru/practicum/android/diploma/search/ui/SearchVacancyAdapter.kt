package ru.practicum.android.diploma.search.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.search.domain.model.SimpleVacancy

class SearchVacancyAdapter(
    private val itemClickListener: ItemClickListener,
) : ListAdapter<SimpleVacancy, RecyclerView.ViewHolder>(VacancyDiffCallBack()) {

    private var vacancyList: MutableList<SimpleVacancy> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<SimpleVacancy>) {
        vacancyList.clear()
        vacancyList = items.toMutableList()
        notifyDataSetChanged()
    }

    private fun filterVacanciesByUniqueNames(vacancies: List<SimpleVacancy>): List<SimpleVacancy> {
        val uniqueNamesMap = mutableMapOf<String, SimpleVacancy>()
        for (vacancy in vacancies) uniqueNamesMap[vacancy.id] = vacancy
        return uniqueNamesMap.values.toList()
    }

    fun addItemsInRecycler(newItems: List<SimpleVacancy>) {
        val filteredItems = filterVacanciesByUniqueNames(newItems)
        val startPosition = vacancyList.size
        vacancyList.addAll(filteredItems)
        notifyItemRangeInserted(startPosition, filteredItems.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVacancyViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return SearchVacancyViewHolder(ItemVacancyBinding.inflate(layoutInspector, parent, false)) { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                vacancyList.getOrNull(position)?.let { vacancy ->
                    itemClickListener.onItemClick(vacancy)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        vacancyList.getOrNull(position)?.let { vacancy ->
            (holder as SearchVacancyViewHolder).bind(vacancy)
        }
    }

    override fun getItemCount() = vacancyList.size

    fun interface ItemClickListener {
        fun onItemClick(vacancy: SimpleVacancy)
    }

    class VacancyDiffCallBack : DiffUtil.ItemCallback<SimpleVacancy>() {
        override fun areItemsTheSame(oldItem: SimpleVacancy, newItem: SimpleVacancy): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SimpleVacancy, newItem: SimpleVacancy): Boolean {
            return oldItem == newItem
        }
    }
}
