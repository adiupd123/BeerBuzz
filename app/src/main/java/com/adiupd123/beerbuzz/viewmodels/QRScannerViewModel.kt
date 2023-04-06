package com.adiupd123.beerbuzz.viewmodels

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiupd123.beerbuzz.repository.BeerRepository
import com.adiupd123.beerbuzz.utils.Constants.TAG2
import com.adiupd123.beerbuzz.utils.NetworkResult
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRScannerViewModel @Inject constructor(private val beerRepository: BeerRepository): ViewModel() {
    val scannedBeerLiveData
    get() = beerRepository.scannedBeerLiveData

    fun getScannedBeer(id: Int){
        viewModelScope.launch {
            beerRepository.getScannedBeer(id)
        }
    }
}