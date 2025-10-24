package com.pulz.moviedeck.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulz.moviedeck.BuildConfig
import com.pulz.moviedeck.data.model.MovieItem
import com.pulz.moviedeck.data.repository.ApiResult
import com.pulz.moviedeck.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    // Estados reativos do Compose (mutableStateOf)
    var query by mutableStateOf("")
        private set

    var movies by mutableStateOf<List<MovieItem>>(emptyList())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    /** Atualiza a query sem disparar a busca automaticamente */
    fun updateQuery(newQuery: String) {
        query = newQuery
    }

    /**
     * Busca filmes (usa query atual se não passar parâmetro).
     * Garante isLoading true enquanto estiver rodando e reseta mensagens anteriores.
     */
    fun searchMovies(searchQuery: String = query) {
        query = searchQuery
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val apiKey = BuildConfig.OMDB_API_KEY
            if (apiKey.isEmpty()) {
                val msg = "Chave da API não configurada. Abra local.properties e adicione OMDB_API_KEY."
                errorMessage = msg
                Log.e("MovieDeck", msg)
                isLoading = false
                return@launch
            }

            try {
                when (val result = repository.searchMovies(apiKey, query)) {
                    is ApiResult.Success -> {
                        val list = result.data.search ?: emptyList()
                        movies = list
                        if (list.isEmpty()) {
                            errorMessage = "Nenhum resultado encontrado para \"$query\"."
                        } else {
                            errorMessage = null
                        }
                    }
                    is ApiResult.ApiError -> {
                        errorMessage = result.message ?: "Nenhum resultado encontrado."
                        movies = emptyList()
                        Log.e("MovieDeck", "API error: ${result.message}")
                    }
                    is ApiResult.HttpError -> {
                        errorMessage = when (result.code) {
                            401 -> "Chave de API inválida (401). Verifique sua chave."
                            403 -> "Acesso proibido (403)."
                            404 -> "Recurso não encontrado (404)."
                            500 -> "Erro no servidor. Tente novamente mais tarde."
                            else -> "Erro HTTP: ${result.code}. Tente novamente."
                        }
                        movies = emptyList()
                        Log.e("MovieDeck", "HTTP error ${result.code}: ${result.message}")
                    }
                    is ApiResult.NetworkError -> {
                        errorMessage = "Sem conexão — verifique sua internet e tente novamente."
                        movies = emptyList()
                        Log.e("MovieDeck", "Network error")
                    }
                    is ApiResult.UnknownError -> {
                        errorMessage = result.error ?: "Erro desconhecido. Tente novamente."
                        movies = emptyList()
                        Log.e("MovieDeck", "Unknown error: ${result.error}")
                    }
                }
            } finally {
                isLoading = false
            }
        }
    }
}
