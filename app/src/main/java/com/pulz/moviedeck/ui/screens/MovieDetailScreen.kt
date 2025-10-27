package com.pulz.moviedeck.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // busca automática
    LaunchedEffect(imdbID) {
        viewModel.fetchMovieDetail(imdbID)
    }

    Scaffold(
        topBar = {
            // Deixamos a TopAppBar transparente para o fundo aparecer
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent, // Cor de fundo transparente
                    scrolledContainerColor = MaterialTheme.colorScheme.surface // Cor ao rolar (opcional)
                )
            )
        }
    ) { padding ->
        // Box é a chave para sobrepor elementos
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding()) // Adiciona padding apenas embaixo
        ) {
            // 1. IMAGEM DE FUNDO
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie?.poster)
                    .crossfade(true)
                    .build(),
                contentDescription = "Plano de fundo do filme",
                contentScale = ContentScale.Crop, // Garante que a imagem cubra todo o espaço
                modifier = Modifier
                    .fillMaxSize()
                    // 2. SCRIM (GRADIENTE)
                    // Aplica um gradiente escuro de baixo para cima para garantir a legibilidade do texto
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                                startY = size.height / 3,
                                endY = size.height
                            )
                        )
                    }
            )

            // Coluna para o conteúdo principal, ocupando o espaço da TopAppBar
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()) // Respeita o espaço da TopAppBar
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Centraliza o conteúdo
            ) {
                // Trata os estados de loading e erro
                when {
                    isLoading -> CircularProgressIndicator()
                    error != null -> Text("Erro: $error", color = MaterialTheme.colorScheme.onError)
                    movie != null -> {
                        // 3. CONTEÚDO PRINCIPAL (MovieCard)
                        MovieCard(
                            movie = movie!!,
                            modifier = Modifier
                                .fillMaxWidth(0.8f) // Ocupa 80% da largura para um visual melhor
                        )
                    }
                }
            }
        }
    }
}
