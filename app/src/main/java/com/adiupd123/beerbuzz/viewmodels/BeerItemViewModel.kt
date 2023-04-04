package com.adiupd123.beerbuzz.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiupd123.beerbuzz.models.local.FavouriteBeer
import com.adiupd123.beerbuzz.repository.BeerRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerItemViewModel @Inject constructor(private val beerRemoteRepository: BeerRemoteRepository): ViewModel() {

    private var _isBeerLiked = MutableLiveData<Boolean>()
    val isBeerLiked: LiveData<Boolean> get() = _isBeerLiked

    fun checkIfBeerFavourite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isBeerLiked.postValue(beerRemoteRepository.isBeerFavourite(id))
        }
    }
    fun removeFavouriteBeer(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            beerRemoteRepository.removeFavouriteBeer(id)
        }
        _isBeerLiked.postValue(false)
    }
    fun addFavouriteBeer(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            beerRemoteRepository.addFavouriteBeer(id)
        }
        _isBeerLiked.postValue(true)
    }
}