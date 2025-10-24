package com.pulz.moviedeck.data.api

import com.pulz.moviedeck.data.model.MovieItem
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

    /**
     * Busca detalhes de um filme específico pelo IMDb ID.
     *
     * @param apiKey Chave da OMDb API.
     * @param imdbID ID único do filme (ex: "tt0372784").
     * @return Response contendo MovieItem (detalhes do filme).
     */
    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") imdbID: String
    ): Response<MovieItem>
}
