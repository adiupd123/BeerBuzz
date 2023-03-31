package com.adiupd123.beerbuzz.repository

import android.util.Log
import com.adiupd123.beerbuzz.api.BeerApi
import com.adiupd123.beerbuzz.utils.Constants.TAG
import javax.inject.Inject

class BeerRemoteRepository @Inject constructor(private val beerApi: BeerApi) {
    suspend fun getAllBeers(page: Int, per_page: Int){
        val response = beerApi.getAllBeers(1, 20)
        Log.d(TAG, response.toString())
    }

    suspend fun getBeers(beerName: String, page: Int, per_page: Int){
        val response = beerApi.getBeers(beerName, page, per_page)
        Log.d(TAG, response.toString())
    }
}