package com.pulz.moviedeck.data.api

import com.pulz.moviedeck.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface principal para consumo da OMDb API.
 */
interface OmdbApi {

    /**
     * Busca filmes pela API.
     *
     * @param apiKey Chave da OMDb API.
     * @param searchQuery Termo de busca (ex: "batman").
     * @return Response contendo MovieResponse (lista de filmes e metadados).
     */
    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") searchQuery: String
    ): Response<MovieResponse>
}
