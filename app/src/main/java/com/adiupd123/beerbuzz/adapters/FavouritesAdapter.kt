package com.adiupd123.beerbuzz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adiupd123.beerbuzz.databinding.BeerItemBinding
import com.adiupd123.beerbuzz.databinding.FavBeerItemBinding
import com.adiupd123.beerbuzz.models.local.FavouriteBeer
import com.adiupd123.beerbuzz.models.remote.BeersResponseItem
import com.bumptech.glide.Glide

class FavouritesAdapter() : androidx.recyclerview.widget.ListAdapter<FavouriteBeer, FavouritesAdapter.FavouriteBeerViewHolder>(ComparatorDiffUtil()) {

    inner class FavouriteBeerViewHolder(private val binding: FavBeerItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(beerItem: FavouriteBeer){
            // Change BeerItem used for rv item
            binding.beerIdTextView.text = "# ${beerItem.beerId}"
            binding.beerNameTextView.text = beerItem.beerName
//            binding.root.setOnClickListener{
//                onFavBeerItemClicked(beerItem)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteBeerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavBeerItemBinding.inflate(inflater, parent, false);
        return FavouriteBeerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteBeerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    class ComparatorDiffUtil: DiffUtil.ItemCallback<FavouriteBeer>() {
        override fun areItemsTheSame(oldItem: FavouriteBeer, newItem: FavouriteBeer): Boolean {
            return oldItem.beerId == newItem.beerId
        }

        override fun areContentsTheSame(oldItem: FavouriteBeer, newItem: FavouriteBeer): Boolean {
            return oldItem == newItem
        }

    }
}