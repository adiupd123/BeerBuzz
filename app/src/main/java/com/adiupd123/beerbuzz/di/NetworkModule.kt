package com.adiupd123.beerbuzz.di

import com.adiupd123.beerbuzz.api.BeerApi
import com.adiupd123.beerbuzz.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }
    @Provides
    @Singleton
    fun provideBeerApi(retrofit: Retrofit): BeerApi {
        return retrofit.create(BeerApi::class.java)
    }

}