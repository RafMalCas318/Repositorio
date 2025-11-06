package com.example.cristopeliculeitor.ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cristopeliculeitor.data.model.*
import com.example.cristopeliculeitor.viewmodel.Modelo
import kotlinx.coroutines.delay

// 🧩 Componente individual de Serie
@Composable
fun SerieItem(
    title: String?,
    posterPath: String?,
    onSerieClick: () -> Unit
) {
    val baseUrl = "https://image.tmdb.org/t/p/w500"
    val fullImageUrl = if (!posterPath.isNullOrEmpty()) "$baseUrl$posterPath" else ""

    Card(
        modifier = Modifier
            .size(width = 150.dp, height = 220.dp)
            .padding(8.dp)
            .clickable(onClick = onSerieClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = fullImageUrl,
                contentDescription = title ?: "Sin título",
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = title ?: "Sin título",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)
            )
        }
    }
}

// ✨ Shimmer personalizado (sin accompanist)
@Composable
fun SerieItemPlaceholder() {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val shimmerX = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shimmerX"
    )

    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = androidx.compose.ui.geometry.Offset(shimmerX.value - 200f, 0f),
        end = androidx.compose.ui.geometry.Offset(shimmerX.value, 200f)
    )

    Card(
        modifier = Modifier
            .size(width = 150.dp, height = 220.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(brush),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {}
}

// 🎬 Pantalla principal de Series
@Composable
fun SeriesScreen(
    modelo: Modelo = viewModel(),
    onSerieClick: (Int, String) -> Unit // (ID, Tipo: "serie")
) {
    val populares by modelo.seriePopular.collectAsState()
    val enEmision by modelo.serieEnEmision.collectAsState()
    val mejorValoradas by modelo.serieMejorValorada.collectAsState()

    var isLoading by remember { mutableStateOf(true) }

    // 🔸 Cargar solo una vez
    LaunchedEffect(Unit) {
        modelo.cargarSeriePopular()
        modelo.cargarSerieEnEmision()
        modelo.cargarSerieMejorValorada()
        delay(700) // pequeña espera para transición sin parpadeo
        isLoading = false
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(bottom = 16.dp)) {

        // --- SERIES POPULARES ---
        Text(
            text = "Series Populares",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        )

        if (isLoading) {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(6) { SerieItemPlaceholder() }
            }
        } else {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(populares) { serie: TvP ->
                    SerieItem(
                        title = serie.name ?: "Sin título",
                        posterPath = serie.posterPath,
                        onSerieClick = { onSerieClick(serie.id, "serie") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SERIES EN EMISIÓN ---
        Text(
            text = "Series En Emisión",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )

        if (isLoading) {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(6) { SerieItemPlaceholder() }
            }
        } else {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(enEmision) { serie: TvEnEmision ->
                    SerieItem(
                        title = serie.name ?: "Sin título",
                        posterPath = serie.posterPath,
                        onSerieClick = { onSerieClick(serie.id, "serie") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SERIES MEJOR VALORADAS ---
        Text(
            text = "Series Mejor Valoradas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )

        if (isLoading) {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(6) { SerieItemPlaceholder() }
            }
        } else {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(mejorValoradas) { serie: TvValoradas ->
                    SerieItem(
                        title = serie.name ?: "Sin título",
                        posterPath = serie.posterPath,
                        onSerieClick = { onSerieClick(serie.id, "serie") }
                    )
                }
            }
        }
    }
}
