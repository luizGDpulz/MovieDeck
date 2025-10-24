package com.pulz.moviedeck.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pulz.moviedeck.data.repository.MovieRepository

class MovieViewModelFactory(
    private val repository: MovieRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
