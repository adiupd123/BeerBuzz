package com.adiupd123.beerbuzz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.adiupd123.beerbuzz.databinding.FragmentBeerItemBinding
import com.adiupd123.beerbuzz.models.remote.BeersResponseItem
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeerItemFragment : Fragment() {
    private var _binding: FragmentBeerItemBinding ?= null
    private val binding get() = _binding!!
    private var beerItem: BeersResponseItem? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBeerItemBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jsonBeerItem = arguments?.getString("beerItem")
        if(jsonBeerItem != null){
            beerItem = Gson().fromJson(jsonBeerItem, BeersResponseItem::class.java)
            beerItem?.let {
                binding.beerIdTextView.text = "#" + it.id
                binding.beerNameTextView.text = it.name
                Glide.with(binding.beerImageView)
                    .load(it.image_url)
                    .placeholder(R.drawable.beer_item_image)
                    .into(binding.beerImageView)
                binding.beerDescTextView.text = it.description
                binding.fbValTV.text = it.first_brewed
                val ingredients = "Hops: " + it.ingredients.hops.toString() + "\n" +
                        "Malts: " + it.ingredients.malt.toString() + "\n" +
                        "Yeast: " + it.ingredients.yeast.toString()
                binding.ingredientsValTV.text = ingredients
                val volume = it.volume.value.toString() + " " + it.volume.unit
                binding.volValTV.text = volume
            }
        } else{
            Toast.makeText(requireContext(), "Could not retrieve data", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}