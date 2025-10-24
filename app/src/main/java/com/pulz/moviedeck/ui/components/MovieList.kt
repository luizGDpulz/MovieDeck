package com.pulz.moviedeck.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pulz.moviedeck.data.model.MovieItem

@Composable
fun MovieList(movies: List<MovieItem>, onMovieClick: (MovieItem) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(movies, key = { it.imdbID!! }) { movie ->
            MovieCard(
                movie = movie,
                modifier = Modifier.clickable { onMovieClick(movie) }
            )
        }
    }
}