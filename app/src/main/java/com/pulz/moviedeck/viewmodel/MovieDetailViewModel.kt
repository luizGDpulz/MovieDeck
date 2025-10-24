package com.pulz.moviedeck.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulz.moviedeck.data.model.MovieItem
import com.pulz.moviedeck.data.repository.ApiResult
import com.pulz.moviedeck.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movie = MutableStateFlow<MovieItem?>(null)
    val movie: StateFlow<MovieItem?> = _movie

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchMovieDetail(imdbID: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            when (val result = repository.getMovieDetails(imdbID)) {
                is ApiResult.Success -> {
                    _movie.value = result.data
                }

                is ApiResult.ApiError -> {
                    _error.value = result.message ?: "Erro retornado pela API."
                }

                is ApiResult.HttpError -> {
                    _error.value = "Erro HTTP ${result.code}: ${result.message ?: "sem mensagem"}"
                }

                is ApiResult.NetworkError -> {
                    _error.value = "Falha de rede. Verifique sua conexão."
                }

                is ApiResult.UnknownError -> {
                    _error.value = (result.message ?: "Erro desconhecido ao carregar o filme.") as String?
                }
            }

            _isLoading.value = false
        }
    }
}
