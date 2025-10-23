package com.pulz.moviedeck.data.repository

import android.util.Log
import com.pulz.moviedeck.data.api.OmdbApi
import com.pulz.moviedeck.data.model.MovieResponse
import retrofit2.Response
import java.io.IOException
import retrofit2.HttpException

class MovieRepository(private val api: OmdbApi) {
    /**
     * Busca filmes usando a OMDb API.
     * @param apiKey Chave da OMDb API.
     * @param query Termo de pesquisa.
     * @return Response<MovieResponse> ou null em caso de falha.
     */
    suspend fun searchMovies(apiKey: String, query: String): Response<MovieResponse>? {
        return try {
            api.searchMovies(apiKey, query)
        } catch (e: IOException) {
            Log.e("MovieDeck", "Erro de rede: ${e.localizedMessage}")
            null
        } catch (e: HttpException) {
            Log.e("MovieDeck", "Erro HTTP: ${e.code()} - ${e.message()}")
            null
        } catch (e: Exception) {
            Log.e("MovieDeck", "Erro inesperado: ${e.localizedMessage}")
            null
        }
    }
}