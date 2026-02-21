package com.example.theguide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theguide.databinding.ItemBannerBinding
import com.bumptech.glide.Glide

class BannerAdapter(
    private val onClick: (Banner) -> Unit = {}
) : RecyclerView.Adapter<BannerAdapter.VH>() {

    private val items = mutableListOf<Banner>()

    fun submitList(newItems: List<Banner>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class VH(val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val b = items[position]
        holder.binding.bannerTitle.text = b.title
        holder.binding.bannerSubtitle.text = b.subtitle

        if (b.imageUrl.isNotBlank()) {
            Glide.with(holder.itemView)
                .load(b.imageUrl)
                .centerCrop()
                .into(holder.binding.bannerImage)
        } else {
            holder.binding.bannerImage.setImageDrawable(null)
        }

        holder.binding.root.setOnClickListener { onClick(b) }
    }
}