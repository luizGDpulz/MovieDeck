package com.pulz.moviedeck.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pulz.moviedeck.data.model.MovieItem
import com.pulz.moviedeck.ui.components.MovieList
import com.pulz.moviedeck.ui.components.SearchBar
import com.pulz.moviedeck.viewmodel.MovieViewModel

@Composable
fun HomeScreen(
    movieViewModel: MovieViewModel,
    onMovieClick: (MovieItem) -> Unit
) {
    val query = movieViewModel.query
    val movies = movieViewModel.movies
    val error = movieViewModel.errorMessage
    val isLoading = movieViewModel.isLoading

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "🎬 MovieDeck",
                style = MaterialTheme.typography.headlineMedium
            )

            SearchBar(
                query = query,
                onQueryChange = { movieViewModel.updateQuery(it) },
                onSearch = { movieViewModel.searchMovies() },
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            when {
                isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(8.dp))
                        Text("Carregando filmes...")
                    }
                }

                error != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { movieViewModel.searchMovies() }) {
                            Text("Tentar novamente")
                        }
                    }
                }

                movies.isNotEmpty() -> {
                    MovieList(movies, onMovieClick)
                }
            }
        }
    }
}
