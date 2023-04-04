package com.adiupd123.beerbuzz.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiupd123.beerbuzz.models.remote.BeersResponse
import com.adiupd123.beerbuzz.repository.BeerRemoteRepository
import com.adiupd123.beerbuzz.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val beerRemoteRepository: BeerRemoteRepository): ViewModel() {
    val allbeersLiveData
    get() = beerRemoteRepository.allBeersLiveData
    var currentPage = 1

    val allFavouriteBeersLiveData
    get() = beerRemoteRepository.allFavouriteBeerLiveData

    fun loadMoreData() {
        getAllBeers(++currentPage)
    }
    fun getAllBeers(currentPage: Int){
        viewModelScope.launch(Dispatchers.IO) {
            beerRemoteRepository.getAllBeers(currentPage, 10)
        }
    }

    fun getSearchedBeers(beerName: String, page: Int, per_page: Int){
        viewModelScope.launch {
            beerRemoteRepository.getSearchedBeers(beerName, page, per_page)
        }
    }

    fun getAllFavouriteBeers(){
        viewModelScope.launch(Dispatchers.IO) {
            beerRemoteRepository.showAllFavouriteBeers()
        }
    }


}