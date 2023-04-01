package com.adiupd123.beerbuzz.api

import com.adiupd123.beerbuzz.models.remote.BeersResponse
import com.adiupd123.beerbuzz.models.remote.BeersResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BeerApi {

    // https://api.punkapi.com/v2/beers and
    // then add ?brewed_before=11-2012&abv_gt=6 - we'll implement additional params when basic features are implemented
    // OR ?page=2&per_page=80 for pagination
    //Get All beers paginated manner
    @GET("/v2/beers")
    suspend fun getAllBeers(@Query("page") page: Int, @Query("per_page") per_page: Int): Response<BeersResponse>

    // Get some beers using parameters in a paginated manner when searched
    @GET("/v2/beers")
    suspend fun getSearchedBeers(@Query("beer_name") beer_name: String, @Query("page") page: Int, @Query("per_page") per_page: Int): Response<BeersResponse>

    // Get Random Beer using Random beer button
    @GET("/v2/beers/random")
    suspend fun getRandomBeer(): Response<BeersResponse>

}