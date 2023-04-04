package com.adiupd123.beerbuzz.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_beers")
data class FavouriteBeer(
    @PrimaryKey
    val beerId: Int,
    val beerName: String
)
