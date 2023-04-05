package com.adiupd123.beerbuzz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiupd123.beerbuzz.repository.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val beerRepository: BeerRepository): ViewModel() {
    val allbeersLiveData
    get() = beerRepository.allBeersLiveData
    var allCurrentPage = 1

    val searchedBeersLiveData
    get() = beerRepository.searchedBeersLiveData
    var searchedCurrentPage = 1

    var searchQuery: String = ""

    fun loadMoreAllData() {
        getAllBeers(++allCurrentPage)
    }

    fun loadMoreSearchedData(searchQuery: String){
        getSearchedBeers(searchQuery, ++searchedCurrentPage)
    }

    fun getAllBeers(currentPage: Int){
        viewModelScope.launch(Dispatchers.IO) {
            beerRepository.getAllBeers(currentPage, 10)
        }
    }

    fun getSearchedBeers(beerName: String, page: Int){
        viewModelScope.launch(Dispatchers.IO) {
            beerRepository.getSearchedBeers(beerName, page, 10)
        }
    }
}