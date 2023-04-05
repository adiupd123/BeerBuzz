package com.adiupd123.beerbuzz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.adiupd123.beerbuzz.adapters.FavouritesAdapter
import com.adiupd123.beerbuzz.databinding.FragmentFavouritesBinding
import com.adiupd123.beerbuzz.databinding.FragmentMainBinding
import com.adiupd123.beerbuzz.models.local.FavouriteBeer
import com.adiupd123.beerbuzz.models.local.FavouriteBeersList
import com.adiupd123.beerbuzz.models.remote.BeersResponseItem
import com.adiupd123.beerbuzz.utils.NetworkResult
import com.adiupd123.beerbuzz.viewmodels.FavouriteViewModel
import com.adiupd123.beerbuzz.viewmodels.MainViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment() {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private val favViewModel by viewModels<FavouriteViewModel>()
    private var allFavBeersList: List<FavouriteBeer?> = emptyList()
    private lateinit var favBeersAdapter: FavouritesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        favBeersAdapter = FavouritesAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
        favViewModel.getAllFavouriteBeers()
        binding.beerFavsRecyclerView.apply {
            adapter = favBeersAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun bindObservers(){
        favViewModel.allFavouriteBeersLiveData.observe(viewLifecycleOwner, Observer {
            allFavBeersList = it
            favBeersAdapter.submitList(allFavBeersList)
        })
    }
}