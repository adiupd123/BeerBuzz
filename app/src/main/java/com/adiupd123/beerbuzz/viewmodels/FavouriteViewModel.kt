package com.adiupd123.beerbuzz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiupd123.beerbuzz.repository.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val beerRepository: BeerRepository): ViewModel() {
    val allFavouriteBeersLiveData
        get()= beerRepository.allFavouriteBeersLiveData

    fun getAllFavouriteBeers(){
        viewModelScope.launch(Dispatchers.IO) {
            beerRepository.showAllFavouriteBeers()
        }
    }
}