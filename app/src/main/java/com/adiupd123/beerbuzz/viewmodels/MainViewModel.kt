package com.adiupd123.beerbuzz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiupd123.beerbuzz.repository.BeerRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val beerRemoteRepository: BeerRemoteRepository): ViewModel() {

    fun getRandomBeer(){
        viewModelScope.launch {
            beerRemoteRepository.getRandomBeer()
        }
    }
}