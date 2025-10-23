package com.pulz.moviedeck.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulz.moviedeck.BuildConfig
import com.pulz.moviedeck.data.api.RetrofitClient
import com.pulz.moviedeck.data.model.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val _movies = MutableStateFlow<List<MovieItem>>(emptyList())
    val movies: StateFlow<List<MovieItem>> = _movies

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun testOmdbApi() {
        viewModelScope.launch {
            try {
                val apiKey = BuildConfig.OMDB_API_KEY
                if (apiKey.isEmpty()) {
                    _errorMessage.value = "API Key vazia! Configure no local.properties."
                    Log.e("MovieDeck", "❌ API Key vazia. Configure no local.properties!")
                    return@launch
                }

                val response = RetrofitClient.api.searchMovies(apiKey, "star wars")

                if (response.isSuccessful) {
                    val result = response.body()
                    val movieList = result?.search ?: emptyList()
                    _movies.value = movieList
                    Log.d("MovieDeck", "✅ ${movieList.size} filmes encontrados")
                } else {
                    val err = "Erro da API: ${response.code()} - ${response.message()}"
                    _errorMessage.value = err
                    Log.e("MovieDeck", err)
                }
            } catch (e: Exception) {
                val msg = "💥 Erro: ${e.localizedMessage}"
                _errorMessage.value = msg
                Log.e("MovieDeck", msg, e)
            }
        }
    }
}
