package com.pulz.moviedeck.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pulz.moviedeck.data.model.MovieItem

@Composable
fun MovieList(movies: List<MovieItem>, onMovieClick: (MovieItem) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(movies) { movie ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMovieClick(movie) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = movie.title ?: "Sem título",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = movie.year ?: "Ano desconhecido",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
