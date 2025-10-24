package com.pulz.moviedeck.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pulz.moviedeck.ui.components.MovieCard
import com.pulz.moviedeck.viewmodel.MovieDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    imdbID: String,
    viewModel: MovieDetailViewModel,
    onBack: () -> Unit
) {
    val movie by viewModel.movie.collectAsState()

    // busca automática
    LaunchedEffect(imdbID) {
        viewModel.fetchMovieDetail(imdbID)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(movie?.title ?: "Detalhes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            movie?.let {
                MovieCard(it, modifier = Modifier.fillMaxWidth())
            } ?: Text("Carregando detalhes...", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
