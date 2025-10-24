package com.pulz.moviedeck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pulz.moviedeck.data.api.RetrofitClient
import com.pulz.moviedeck.data.model.MovieItem
import com.pulz.moviedeck.data.repository.MovieRepository
import com.pulz.moviedeck.ui.components.SearchBar
import com.pulz.moviedeck.ui.theme.MovieDeckTheme
import com.pulz.moviedeck.viewmodel.MovieViewModel
import com.pulz.moviedeck.viewmodel.MovieViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieDeckTheme {
                val repository = MovieRepository(RetrofitClient.api)
                val factory = MovieViewModelFactory(repository)
                val movieViewModel: MovieViewModel = viewModel(factory = factory)

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

                        // Chamada do Search bar composable
                        SearchBar(
                            query = query,
                            onQueryChange = { movieViewModel.updateQuery(it) },
                            onSearch = { movieViewModel.searchMovies() },
                            isLoading = isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Status / resultados
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
                                MovieList(movies)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieList(movies: List<MovieItem>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(movies) { movie ->
            Card(
                modifier = Modifier.fillMaxWidth(),
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
