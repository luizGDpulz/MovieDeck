package com.pulz.moviedeck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pulz.moviedeck.data.api.RetrofitClient
import com.pulz.moviedeck.data.model.MovieItem
import com.pulz.moviedeck.data.repository.MovieRepository
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

                val movies by movieViewModel.movies.collectAsState()
                val error by movieViewModel.errorMessage.collectAsState()

                // Faz a chamada ao iniciar
                LaunchedEffect(Unit) {
                    movieViewModel.searchMovies("star wars")
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "🎬 MovieDeck",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(Modifier.height(16.dp))

                        when {
                            error != null -> {
                                Column {
                                    Text("Erro: $error", color = MaterialTheme.colorScheme.error)
                                    Spacer(Modifier.height(8.dp))
                                    Button(onClick = { movieViewModel.searchMovies("star wars") }) {
                                        Text("Tentar novamente")
                                    }
                                }
                            }
                            movies.isEmpty() -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator()
                                    Spacer(Modifier.height(8.dp))
                                    Text("Carregando filmes...")
                                }
                            }
                            else -> MovieList(movies)
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
                    Text(text = movie.imdbID ?: "Sem ID", style = MaterialTheme.typography.titleLarge)
                    Text(text = movie.title ?: "Sem título", style = MaterialTheme.typography.titleMedium)
                    Text(text = movie.year ?: "Ano desconhecido", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
