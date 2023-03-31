package com.adiupd123.beerbuzz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiupd123.beerbuzz.repository.BeerRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerViewModel @Inject constructor(private val beerRemoteRepository: BeerRemoteRepository): ViewModel() {

    fun getAllBeers(page: Int, per_page: Int){
        viewModelScope.launch {
            beerRemoteRepository.getAllBeers(page, per_page)
        }
    }

    fun getSearchedBeers(beerName: String, page: Int, per_page: Int){
        viewModelScope.launch {
            beerRemoteRepository.getBeers(beerName, page, per_page)
        }
    }
}