package com.adiupd123.beerbuzz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiupd123.beerbuzz.repository.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val beerRepository: BeerRepository) : ViewModel() {

    val bOTDLiveData
        get() = beerRepository.botdLiveData

    fun getBeerOfTheDay() {
        viewModelScope.launch(Dispatchers.IO) {
            beerRepository.getBeerOfTheDay()
        }
    }
}