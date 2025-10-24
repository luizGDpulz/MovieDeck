package com.pulz.moviedeck.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulz.moviedeck.BuildConfig
import com.pulz.moviedeck.data.model.MovieItem
import com.pulz.moviedeck.data.repository.ApiResult
import com.pulz.moviedeck.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableStateFlow<List<MovieItem>>(emptyList())
    val movies: StateFlow<List<MovieItem>> = _movies

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * Busca filmes e atualiza os flows com mensagens amigáveis.
     */
    fun searchMovies(query: String) {
        viewModelScope.launch {
            val apiKey = BuildConfig.OMDB_API_KEY
            if (apiKey.isEmpty()) {
                val msg = "Chave da API não configurada. Abra local.properties e adicione OMDB_API_KEY."
                _errorMessage.value = msg
                Log.e("MovieDeck", msg)
                return@launch
            }

            // Resetar erro anterior
            _errorMessage.value = null

            when (val result = repository.searchMovies(apiKey, query)) {
                is ApiResult.Success -> {
                    val movies = result.data.search ?: emptyList()
                    _movies.value = movies
                }
                is ApiResult.ApiError -> {
                    // API respondeu com Response = "False" (ex: Movie not found!, Invalid API key!)
                    val friendly = result.message ?: "Nenhum resultado encontrado."
                    _errorMessage.value = friendly
                    Log.e("MovieDeck", "API error: $friendly")
                }
                is ApiResult.HttpError -> {
                    val friendly = when (result.code) {
                        401 -> "Chave de API inválida (401). Verifique sua chave."
                        403 -> "Acesso proibido (403)."
                        404 -> "Recurso não encontrado (404)."
                        500 -> "Erro no servidor. Tente novamente mais tarde."
                        else -> "Erro HTTP: ${result.code}. Tente novamente."
                    }
                    _errorMessage.value = friendly
                    Log.e("MovieDeck", "HTTP error ${result.code}: ${result.message}")
                }
                is ApiResult.NetworkError -> {
                    val friendly = "Sem conexão — verifique sua internet e tente novamente."
                    _errorMessage.value = friendly
                    Log.e("MovieDeck", "Network error")
                }
                is ApiResult.UnknownError -> {
                    val friendly = result.error ?: "Erro desconhecido. Tente novamente."
                    _errorMessage.value = friendly
                    Log.e("MovieDeck", "Unknown error: ${result.error}")
                }
            }
        }
    }
}
