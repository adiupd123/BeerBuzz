package com.adiupd123.beerbuzz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiupd123.beerbuzz.repository.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRScannerViewModel @Inject constructor(private val beerRepository: BeerRepository) :
    ViewModel() {
    val scannedBeerLiveData
        get() = beerRepository.scannedBeerLiveData

    fun getScannedBeer(id: Int) {
        viewModelScope.launch {
            beerRepository.getScannedBeer(id)
        }
    }
}