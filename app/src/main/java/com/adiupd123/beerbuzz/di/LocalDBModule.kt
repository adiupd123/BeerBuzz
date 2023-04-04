package com.adiupd123.beerbuzz.di

import android.content.Context
import androidx.room.Room
import com.adiupd123.beerbuzz.db.BeersDatabase
import com.adiupd123.beerbuzz.db.FavouriteDao
import com.adiupd123.beerbuzz.models.local.FavouriteBeer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDBModule {
    @Provides
    @Singleton
    fun providesBeersDatabase(@ApplicationContext context: Context): BeersDatabase {
        return Room.databaseBuilder(
            context,
            BeersDatabase::class.java,
            "beers_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesFavouriteDao(database: BeersDatabase): FavouriteDao {
        return database.favouriteDao()
    }
}