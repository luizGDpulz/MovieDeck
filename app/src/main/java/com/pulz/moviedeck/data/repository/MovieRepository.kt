package com.pulz.moviedeck.data.repository

import android.util.Log
import com.pulz.moviedeck.data.api.OmdbApi
import com.pulz.moviedeck.data.model.MovieResponse
import retrofit2.HttpException
import java.io.IOException

class MovieRepository(private val api: OmdbApi) {
    /**
     * Faz a busca e mapeia os possíveis erros em ApiResult.
     * Busca filmes usando a OMDb API.
     * @param apiKey Chave da OMDb API.
     * @param query Termo de pesquisa.
     * @return Response<MovieResponse> ou null em caso de falha.
     */
    suspend fun searchMovies(apiKey: String, query: String): ApiResult<MovieResponse> {
        return try {
            val response = api.searchMovies(apiKey, query)

            if (!response.isSuccessful) {
                Log.e("MovieDeck", "HTTP error ${response.code()} - ${response.message()}")
                return ApiResult.HttpError(response.code(), response.message())
            }

            val body = response.body()
            if (body == null) {
                Log.e("MovieDeck", "Resposta vazia da API")
                return ApiResult.UnknownError("Resposta vazia da API")
            }

            // A OMDb pode retornar HTTP 200 mas com Response = "False"
            if (body.response?.equals("False", ignoreCase = true) == true) {
                val apiMsg = body.error ?: "Nenhum resultado encontrado."
                Log.e("MovieDeck", "API returned Response=false: $apiMsg")
                return ApiResult.ApiError(apiMsg)
            }

            ApiResult.Success(body)
        } catch (e: IOException) {
            // Sem internet / timeout
            Log.e("MovieDeck", "Erro de rede: ${e.localizedMessage}")
            ApiResult.NetworkError
        } catch (e: HttpException) {
            Log.e("MovieDeck", "HttpException: ${e.code()} ${e.message()}")
            ApiResult.HttpError(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("MovieDeck", "Erro inesperado: ${e.localizedMessage}", e)
            ApiResult.UnknownError(e.localizedMessage)
        }
    }
}
