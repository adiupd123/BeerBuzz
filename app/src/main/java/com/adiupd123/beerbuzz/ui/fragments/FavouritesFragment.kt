package com.adiupd123.beerbuzz.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.adiupd123.beerbuzz.adapters.FavouritesAdapter
import com.adiupd123.beerbuzz.databinding.FragmentFavouritesBinding
import com.adiupd123.beerbuzz.models.local.FavouriteBeer
import com.adiupd123.beerbuzz.viewmodels.FavouriteViewModel
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

    private fun bindObservers(){
        favViewModel.allFavouriteBeersLiveData.observe(viewLifecycleOwner, Observer {
            allFavBeersList = it
            favBeersAdapter.submitList(allFavBeersList)
        })
    }
}