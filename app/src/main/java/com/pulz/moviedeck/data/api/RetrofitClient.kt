package com.pulz.moviedeck.data.api

import com.pulz.moviedeck.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val api: OmdbService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.OMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OmdbService::class.java)
    }
}