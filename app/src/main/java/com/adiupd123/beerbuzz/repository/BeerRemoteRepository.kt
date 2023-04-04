package com.adiupd123.beerbuzz.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adiupd123.beerbuzz.api.BeerApi
import com.adiupd123.beerbuzz.db.FavouriteDao
import com.adiupd123.beerbuzz.models.local.FavouriteBeer
import com.adiupd123.beerbuzz.models.remote.BeersResponse
import com.adiupd123.beerbuzz.utils.Constants.TAG
import com.adiupd123.beerbuzz.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import java.util.Objects
import javax.inject.Inject

class BeerRemoteRepository @Inject constructor(private val beerApi: BeerApi, private val favouriteDao: FavouriteDao) {

    private val _allBeersLiveData = MutableLiveData<NetworkResult<BeersResponse>>()
    val allBeersLiveData: LiveData<NetworkResult<BeersResponse>>
    get() = _allBeersLiveData

    private val _allFavouriteBeersLiveData = MutableLiveData<List<FavouriteBeer>>()
    val allFavouriteBeerLiveData: LiveData<List<FavouriteBeer>>
    get() = _allFavouriteBeersLiveData


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
    
    suspend fun getRandomBeer(){
        val response = beerApi.getRandomBeer()
        Log.d(TAG, response.body().toString())
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


    suspend fun showAllFavouriteBeers(){
        _allFavouriteBeersLiveData.postValue(favouriteDao.getAllFavouriteBeers())
    }
}