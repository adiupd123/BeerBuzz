package com.adiupd123.beerbuzz.db

import androidx.room.*
import com.adiupd123.beerbuzz.models.local.FavouriteBeer
import com.adiupd123.beerbuzz.models.local.FavouriteBeersList

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavouriteBeer(favouriteBeer: FavouriteBeer)

    @Delete
    suspend fun removeFavouriteBeer(favouriteBeer: FavouriteBeer)

    @Query("SELECT * FROM favourite_beers")
    suspend fun getAllFavouriteBeers(): List<FavouriteBeer>

    @Query("SELECT * FROM favourite_beers WHERE beerId = :bId")
    suspend fun getSelectedFavBeer(bId: Int): FavouriteBeer?
}