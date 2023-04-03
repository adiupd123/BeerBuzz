package com.adiupd123.beerbuzz

import android.icu.lang.UCharacter.VerticalOrientation
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.adiupd123.beerbuzz.adapters.BeerAdapter
import com.adiupd123.beerbuzz.databinding.FragmentSearchBinding
import com.adiupd123.beerbuzz.models.remote.BeersResponseItem
import com.adiupd123.beerbuzz.utils.NetworkResult
import com.adiupd123.beerbuzz.viewmodels.SearchViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var beerAdapter: BeerAdapter
    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        beerAdapter = BeerAdapter(::onBeerItemClicked)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.beerSearchRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = beerAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition = (binding.beerSearchRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val firstVisibleItemPosition = (binding.beerSearchRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    var totalItemCount = adapter?.itemCount ?: 0
                    if (!isLoading && lastVisibleItemPosition >= totalItemCount - 1 && firstVisibleItemPosition >= 0) {
                        // If the current loading process is not running and the user has scrolled to the end of the list
                        // then trigger a new load
                        isLoading = true
                        searchViewModel.loadMoreData()
                    }
                }
            })
        }
        bindObservers()
        searchViewModel.getAllBeers(1)
    }

    private fun bindObservers() {
        // We bind observer to the livedata and call the observer when any change to livedata happens
        searchViewModel.allbeersLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success -> {
                    var currentList = beerAdapter.currentList.toMutableList()
                    it.data?.let { it1 -> currentList.addAll(it1) }
                    beerAdapter.submitList(currentList)
                    isLoading = false
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                    isLoading = false
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                    isLoading = true
                }
            }
        })
    }
    private fun onBeerItemClicked(beerItem: BeersResponseItem){
        val bundle = Bundle()
        bundle.putString("beerItem", Gson().toJson(beerItem))
        findNavController().navigate(R.id.action_searchFragment_to_beerItemFragment, bundle)
    }
    override fun onResume() {
        super.onResume()
        searchViewModel.currentPage = 1
        beerAdapter.submitList(null)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}