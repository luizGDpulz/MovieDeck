package com.pulz.moviedeck.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pulz.moviedeck.data.model.MovieItem

/**
 * MovieCard – exibe pôster, título e ano do filme.
 *
 * - Usa Coil (AsyncImage) para carregamento de imagem.
 * - Layout limpo e adaptável, baseado em Material3.
 * - Pode ser reutilizado em telas detalhadas.
 */
@Composable
fun MovieCard(
    movie: MovieItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pôster (ou placeholder)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.poster)
                    .crossfade(true)
                    .build(),
                contentDescription = "Pôster do filme ${movie.title}",
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                placeholder = painterResource(android.R.drawable.ic_menu_report_image),
                error = painterResource(android.R.drawable.ic_menu_report_image)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = movie.title ?: "Título indisponível",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = movie.year ?: "Ano desconhecido",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}