package com.pulz.moviedeck.data.api

import com.pulz.moviedeck.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton Retrofit para consumir a OmdbApi.
 */
object RetrofitClient {
    val api: OmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.OMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OmdbApi::class.java)
    }
}