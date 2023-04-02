package com.adiupd123.beerbuzz.adapters

import android.content.Context
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adiupd123.beerbuzz.databinding.BeerItemBinding
import com.adiupd123.beerbuzz.models.remote.BeersResponseItem
import com.bumptech.glide.Glide

class BeerAdapter(context: Context): ListAdapter<BeersResponseItem, BeerAdapter.BeerViewHolder>(ComparatorDiffUtil()) {


    inner class BeerViewHolder(private val binding: BeerItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(beer: BeersResponseItem){
            binding.beerIdTextView.text = beer.id.toString()
            Glide.with()
        }
    }
    class ComparatorDiffUtil: DiffUtil.ItemCallback<BeersResponseItem>() {
        override fun areItemsTheSame(oldItem: BeersResponseItem, newItem: BeersResponseItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BeersResponseItem, newItem: BeersResponseItem): Boolean {
            return oldItem == newItem
        }

    }
}