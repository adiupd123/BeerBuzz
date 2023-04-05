package com.adiupd123.beerbuzz.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.adiupd123.beerbuzz.R
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
    ): View {
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
                    val totalItemCount = adapter?.itemCount ?: 0
                    if (!isLoading && lastVisibleItemPosition >= totalItemCount - 1 && firstVisibleItemPosition >= 0) {
                        // If the current loading process is not running and the user has scrolled to the end of the list
                        // then trigger a new load
                        isLoading = true
                        if (searchViewModel.searchQuery == "") {
                            searchViewModel.searchedCurrentPage = 1
                            searchViewModel.loadMoreAllData()
                        } else {
                            searchViewModel.allCurrentPage = 0
                            searchViewModel.loadMoreSearchedData(searchViewModel.searchQuery)
                        }
                    }
                }
            })
        }
        bindObservers()
        searchViewModel.getAllBeers(1)
        binding.searchEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
                searchViewModel.searchQuery = s.toString().trim()
                if(searchViewModel.searchQuery.isNotEmpty()){
                    searchViewModel.getSearchedBeers(searchViewModel.searchQuery, searchViewModel.searchedCurrentPage)
                } else {
                    searchViewModel.getAllBeers(searchViewModel.allCurrentPage)
                }
            }

        })
    }

    @SuppressLint("ShowToast")
    private fun bindObservers() {
        // We bind observer to the livedata and call the observer when any change to livedata happens
        searchViewModel.allbeersLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success -> {
                    if(searchViewModel.allCurrentPage > 1) {
                        val currentList = beerAdapter.currentList.toMutableList()
                        it.data?.let { it1 -> currentList.addAll(it1) }
                        beerAdapter.submitList(currentList)
                    } else {
                        beerAdapter.submitList(it.data)
                    }
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

        searchViewModel.searchedBeersLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success -> {
                    if(searchViewModel.searchedCurrentPage > 1) {
                        val currentSearchedList = beerAdapter.currentList.toMutableList()
                        it.data?.let { it1 -> currentSearchedList.addAll(it1) }
                        beerAdapter.submitList(currentSearchedList)
                    } else {
                        beerAdapter.submitList(it.data)
                    }
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
        searchViewModel.allCurrentPage = 1
        searchViewModel.searchedCurrentPage = 1
        beerAdapter.submitList(null)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}