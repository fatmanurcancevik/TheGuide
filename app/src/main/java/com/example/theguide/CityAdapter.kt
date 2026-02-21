package com.example.theguide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theguide.databinding.ItemCityBinding

class CityAdapter : RecyclerView.Adapter<CityAdapter.VH>() {

    private val items = mutableListOf<City>()

    fun submitList(newItems: List<City>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class VH(val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val city = items[position]
        holder.binding.title.text = "${city.name} (${city.country})"

        val region = city.region?.takeIf { it.isNotBlank() } ?: "-"
        val pop = city.population?.toString() ?: "-"
        holder.binding.subtitle.text = "$region • Nüfus: $pop"
    }
}