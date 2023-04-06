package com.adiupd123.beerbuzz.viewmodels

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiupd123.beerbuzz.repository.BeerRepository
import com.adiupd123.beerbuzz.utils.Constants.TAG
import com.google.zxing.WriterException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BeerItemViewModel @Inject constructor(private val beerRepository: BeerRepository): ViewModel() {

    private var _isBeerLiked = MutableLiveData<Boolean>()
    val isBeerLiked: LiveData<Boolean> get() = _isBeerLiked

    fun checkIfBeerFavourite(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "checkIfBeerFavourite: ${beerRepository.isBeerFavourite(id)}")
            _isBeerLiked.postValue(beerRepository.isBeerFavourite(id))
        }
    }
    fun removeFavouriteBeer(id: Int, name: String){
        viewModelScope.launch(Dispatchers.IO) {
            beerRepository.removeFavouriteBeer(id, name)
        }
        _isBeerLiked.postValue(false)
    }
    fun addFavouriteBeer(id: Int, name: String){
        viewModelScope.launch(Dispatchers.IO) {
            beerRepository.addFavouriteBeer(id, name)
        }
        _isBeerLiked.postValue(true)
    }

    fun generateQRCode(id: String, size: Int): Bitmap? {
        val b = Bundle()
        b.putString("beerItem", id)
        val qrgEncoder = QRGEncoder(id, b, QRGContents.Type.TEXT, size)
        return try {
            // Getting QR-Code as Bitmap
            qrgEncoder.bitmap
        } catch (e: WriterException) {
            Log.v(TAG, e.toString())
            null
        }
    }
}