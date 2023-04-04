package com.adiupd123.beerbuzz.db

import androidx.room.*
import com.adiupd123.beerbuzz.models.local.FavouriteBeer

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavouriteBeer(favouriteBeer: FavouriteBeer)

    @Delete
    suspend fun removeFavouriteBeer(favouriteBeer: FavouriteBeer)

    @Query("SELECT * FROM favourite_beers")
    suspend fun getAllFavouriteBeers(): List<FavouriteBeer>

    @Query("SELECT * FROM favourite_beers WHERE id = :id")
    suspend fun getSelectedFavBeer(id: Int): FavouriteBeer?
}