package com.adiupd123.beerbuzz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adiupd123.beerbuzz.databinding.BeerItemBinding
import com.adiupd123.beerbuzz.models.remote.BeersResponseItem
import com.bumptech.glide.Glide

class BeerAdapter(private val onBeerItemClicked: (BeersResponseItem) -> Unit) : androidx.recyclerview.widget.ListAdapter<BeersResponseItem, BeerAdapter.BeerViewHolder>(ComparatorDiffUtil()) {

    inner class BeerViewHolder(private val binding: BeerItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(beerItem: BeersResponseItem){
            binding.beerIdTextView.text = "#${beerItem.id}"
            Glide.with(binding.beerItemImageView)
                .load(beerItem.image_url)
                .into(binding.beerItemImageView)
            binding.root.setOnClickListener{
                onBeerItemClicked(beerItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BeerItemBinding.inflate(inflater, parent, false)
        return BeerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
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

