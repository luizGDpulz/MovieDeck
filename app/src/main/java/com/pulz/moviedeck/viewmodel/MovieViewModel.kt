package com.pulz.moviedeck.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulz.moviedeck.BuildConfig
import com.pulz.moviedeck.data.model.MovieItem
import com.pulz.moviedeck.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableStateFlow<List<MovieItem>>(emptyList())
    val movies: StateFlow<List<MovieItem>> = _movies

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun searchMovies(query: String) {
        viewModelScope.launch {
            val apiKey = BuildConfig.OMDB_API_KEY
            if (apiKey.isEmpty()) {
                _errorMessage.value = "API Key vazia! Configure no local.properties."
                return@launch
            }

            val response = repository.searchMovies(apiKey, query)
            if (response == null) {
                _errorMessage.value = "Erro na comunicação com a API"
                return@launch
            }

            if (response.isSuccessful) {
                _movies.value = response.body()?.search ?: emptyList()
            } else {
                _errorMessage.value = "Erro da API: ${response.code()} - ${response.message()}"
            }
        }
    }
}