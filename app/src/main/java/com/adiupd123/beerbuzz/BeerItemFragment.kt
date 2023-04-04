package com.adiupd123.beerbuzz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.adiupd123.beerbuzz.databinding.FragmentBeerItemBinding
import com.adiupd123.beerbuzz.models.remote.BeersResponseItem
import com.adiupd123.beerbuzz.utils.Constants.TAG
import com.adiupd123.beerbuzz.viewmodels.BeerItemViewModel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BeerItemFragment : Fragment() {
    private var _binding: FragmentBeerItemBinding ?= null
    private val binding get() = _binding!!
    private var beerItem: BeersResponseItem? = null
    private val beerItemViewModel by viewModels<BeerItemViewModel>()
    private var isBeerLiked = false

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
        bindObservers()
        setInitialData()
        binding.likeImageView.setOnClickListener{
            onLikeButtonClicked()
        }
    }

    private fun bindObservers() {
        beerItemViewModel.isBeerLiked.observe(viewLifecycleOwner, Observer {
            isBeerLiked = it
            Log.d(TAG, "isBeerLiked: " + isBeerLiked)
            setUpLikeBtn(isBeerLiked)
        })
    }

    private fun setUpLikeBtn(isBeerLiked: Boolean) {
        if(isBeerLiked)
            binding.likeImageView.setImageResource(R.drawable.ic_like_done)
        else
            binding.likeImageView.setImageResource(R.drawable.ic_like)
    }

    fun onLikeButtonClicked() {
        if (isBeerLiked) {
            // Remove the beer from favourites
            beerItemViewModel.removeFavouriteBeer(id)
        } else {
            // Add the beer to favourites
            beerItemViewModel.addFavouriteBeer(id)
        }
    }

    private fun setInitialData() {
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
                beerItemViewModel.checkIfBeerFavourite(it.id)
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