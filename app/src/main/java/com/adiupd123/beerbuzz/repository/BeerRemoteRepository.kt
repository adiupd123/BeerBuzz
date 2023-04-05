package com.adiupd123.beerbuzz.repository

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adiupd123.beerbuzz.api.BeerApi
import com.adiupd123.beerbuzz.db.FavouriteDao
import com.adiupd123.beerbuzz.models.local.FavouriteBeer
import com.adiupd123.beerbuzz.models.local.FavouriteBeersList
import com.adiupd123.beerbuzz.models.remote.BeersResponse
import com.adiupd123.beerbuzz.models.remote.BeersResponseItem
import com.adiupd123.beerbuzz.utils.Constants.TAG
import com.adiupd123.beerbuzz.utils.NetworkResult
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Response
import java.util.Objects
import javax.inject.Inject

class BeerRemoteRepository @Inject constructor(private val beerApi: BeerApi,
                                               private val favouriteDao: FavouriteDao,
                                               private val sharedPref: SharedPreferences
                                               ) {

    private val _allBeersLiveData = MutableLiveData<NetworkResult<BeersResponse>>()
    val allBeersLiveData: LiveData<NetworkResult<BeersResponse>>
    get() = _allBeersLiveData

    private val _allFavouriteBeersLiveData = MutableLiveData<List<FavouriteBeer>>()
    val allFavouriteBeersLiveData: LiveData<List<FavouriteBeer>>
    get() = _allFavouriteBeersLiveData

    private val _botdLiveData = MutableLiveData<NetworkResult<BeersResponse>>()
    val botdLiveData: LiveData<NetworkResult<BeersResponse>>
    get() = _botdLiveData

    suspend fun getAllBeers(page: Int, per_page: Int){
        _allBeersLiveData.postValue(NetworkResult.Loading())
        val response = beerApi.getAllBeers(page, per_page)
        if(response.isSuccessful && response.body() != null){
            Log.d(TAG, response.body().toString())
            _allBeersLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if(response.errorBody() != null){
            Log.d(TAG, response.errorBody().toString())
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _allBeersLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _allBeersLiveData.postValue(NetworkResult.Error("Something went wrong!"))
        }
    }

    suspend fun getSearchedBeers(beerName: String, page: Int, per_page: Int){
        val response = beerApi.getSearchedBeers(beerName, page, per_page)
        Log.d(TAG, response.body().toString())
    }
    
    suspend fun getBeerOfTheDay(){
        val lastExecutionTime = sharedPref.getLong("lastExecutionTime", 0)
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastExecutionTime < 24 * 60 * 60 * 1000) {
            // Less than 24 hours have elapsed, return saved beer details
            val savedBeerJson = sharedPref.getString("beerOfTheDay", null)
            if (savedBeerJson != null) {
                val savedBeer = Gson().fromJson(savedBeerJson, BeersResponse::class.java)
                _botdLiveData.postValue(NetworkResult.Success(savedBeer))
                return
            }
        }
        // 24 hours have elapsed, execute the function and save the beer details
        val beerResponse = beerApi.getRandomBeer()
        if(beerResponse.isSuccessful && beerResponse.body() != null){
            Log.d(TAG, beerResponse.body().toString())
            val beerResponseJson = Gson().toJson(beerResponse.body()!!)
            with (sharedPref.edit()) {
                putString("beerOfTheDay", beerResponseJson)
                putLong("lastExecutionTime", currentTime)
                apply()
            }
            _botdLiveData.postValue(NetworkResult.Success(beerResponse.body()!!))
        } else if(beerResponse.errorBody() != null){
            Log.d(TAG, beerResponse.errorBody().toString())
            val errorObj = JSONObject(beerResponse.errorBody()!!.charStream().readText())
            _botdLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _botdLiveData.postValue(NetworkResult.Error("Something went wrong!"))
        }
    }

    suspend fun showAllFavouriteBeers(){
        _allFavouriteBeersLiveData.postValue(favouriteDao.getAllFavouriteBeers())
    }


    // Manage Error Handling here
    suspend fun isBeerFavourite(id: Int): Boolean {
        return favouriteDao.getSelectedFavBeer(id) != null
    }
    suspend fun addFavouriteBeer(id: Int, name: String){
        favouriteDao.addFavouriteBeer(FavouriteBeer(id, name))
    }

    suspend fun removeFavouriteBeer(id: Int, name: String){
        favouriteDao.removeFavouriteBeer(FavouriteBeer(id, name))
    }
}