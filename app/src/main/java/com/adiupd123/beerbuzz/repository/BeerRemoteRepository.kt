package com.adiupd123.beerbuzz.repository

import android.util.Log
import com.adiupd123.beerbuzz.api.BeerApi
import com.adiupd123.beerbuzz.utils.Constants.TAG
import java.util.Objects
import javax.inject.Inject

class BeerRemoteRepository @Inject constructor(private val beerApi: BeerApi) {
    suspend fun getAllBeers(page: Int, per_page: Int){
        val response = beerApi.getAllBeers(page, per_page)
        Log.d(TAG, response.body().toString())
    }

    suspend fun getSearchedBeers(beerName: String, page: Int, per_page: Int){
        val response = beerApi.getSearchedBeers(beerName, page, per_page)
        Log.d(TAG, response.body().toString())
    }
    
    suspend fun getRandomBeer(){
        val response = beerApi.getRandomBeer()
        Log.d(TAG, response.body().toString())
    }
}