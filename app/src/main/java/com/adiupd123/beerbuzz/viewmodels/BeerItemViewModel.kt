package com.adiupd123.beerbuzz.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiupd123.beerbuzz.repository.BeerRemoteRepository
import com.adiupd123.beerbuzz.utils.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class BeerItemViewModel @Inject constructor(private val beerRemoteRepository: BeerRemoteRepository): ViewModel() {

    private var _isBeerLiked = MutableLiveData<Boolean>()
    val isBeerLiked: LiveData<Boolean> get() = _isBeerLiked

    fun checkIfBeerFavourite(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "checkIfBeerFavourite: ${beerRemoteRepository.isBeerFavourite(id)}")
            _isBeerLiked.postValue(beerRemoteRepository.isBeerFavourite(id))
        }
    }
    fun removeFavouriteBeer(id: Int, name: String){
        viewModelScope.launch(Dispatchers.IO) {
            beerRemoteRepository.removeFavouriteBeer(id, name)
        }
        _isBeerLiked.postValue(false)
    }
    fun addFavouriteBeer(id: Int, name: String){
        viewModelScope.launch(Dispatchers.IO) {
            beerRemoteRepository.addFavouriteBeer(id, name)
        }
        _isBeerLiked.postValue(true)
    }
}