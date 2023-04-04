package com.adiupd123.beerbuzz.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adiupd123.beerbuzz.models.local.FavouriteBeer

@Database(entities = [FavouriteBeer::class], version = 1, exportSchema = false)
abstract class BeersDatabase: RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao
}