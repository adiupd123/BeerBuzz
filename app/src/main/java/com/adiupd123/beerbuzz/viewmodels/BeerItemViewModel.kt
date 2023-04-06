package com.adiupd123.beerbuzz.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adiupd123.beerbuzz.repository.BeerRepository
import com.adiupd123.beerbuzz.utils.Constants.TAG
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
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

    fun generateQRCode(text: String, width: Int, height: Int): Bitmap? {
        try {
            val bitMatrix: BitMatrix = QRCodeWriter().encode(
                text,
                BarcodeFormat.QR_CODE,
                width,
                height,
                null
            )
            val bitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(
                        x,
                        y,
                        if (bitMatrix.get(x, y)) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
                    )
                }
            }
            return bitmap
        } catch (e: WriterException) {
            Log.d("QRCodeGeneratorViewModel", "QR code generation failed: ${e.message}")
            return null
        }
    }
}