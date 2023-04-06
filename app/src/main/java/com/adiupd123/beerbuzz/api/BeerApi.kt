package com.adiupd123.beerbuzz.api

import com.adiupd123.beerbuzz.models.remote.BeersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BeerApi {

    //Get All beers paginated manner
    @GET("/v2/beers")
    suspend fun getAllBeers(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Response<BeersResponse>

    // Get some beers using parameters in a paginated manner when searched
    @GET("/v2/beers")
    suspend fun getSearchedBeers(
        @Query("beer_name") beer_name: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Response<BeersResponse>

    // Get a Random Beer
    @GET("/v2/beers/random")
    suspend fun getRandomBeer(): Response<BeersResponse>

    // Get a beer obtained from id generated from scanned qr code
    @GET("/v2/beers")
    suspend fun getScannedBeer(@Query("ids") id: Int): Response<BeersResponse>
}