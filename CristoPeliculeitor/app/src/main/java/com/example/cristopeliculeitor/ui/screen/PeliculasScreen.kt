package com.example.cristopeliculeitor.ui.screen

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cristopeliculeitor.data.model.*
import com.example.cristopeliculeitor.viewmodel.Modelo
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush


//Item de pelicula.
@Composable
fun PeliculaItem(
    title: String,
    posterPath: String?,
    onPeliculaClick: () -> Unit
) {
    val baseUrl = "https://image.tmdb.org/t/p/w500"
    val fullImageUrl = if (posterPath != null) "$baseUrl$posterPath" else ""

    Card(
        modifier = Modifier
            .size(width = 150.dp, height = 220.dp)
            .padding(8.dp)
            .clickable(onClick = onPeliculaClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = fullImageUrl,
                contentDescription = title,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)
            )
        }
    }
}


//Evitar parpadeo de la pantalla.
@Composable
fun PeliculaItemPlaceholder() {
    // 🔹 Animación del brillo del shimmer
    val transition = rememberInfiniteTransition(label = "shimmer")
    val shimmerX = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shimmerX"
    )

    // 🔹 Crea el gradiente que se mueve
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


// Pantalla principal
@Composable
fun PeliculasScreen(
    modelo: Modelo = viewModel(),
    onPeliculaClick: (Int, String) -> Unit
) {
    val populares by modelo.peliculaPopular.collectAsState()
    val proximamente by modelo.peliculaProximamente.collectAsState()
    val mejorValoradas by modelo.peliculaMejorValorada.collectAsState()

    var isLoading by remember { mutableStateOf(true) }

    // Carga inicial (solo una vez)
    LaunchedEffect(Unit) {
        modelo.cargarPeliculaPopular()
        modelo.cargarPeliculaProximamente()
        modelo.cargarPeliculaMejorValorada()
        kotlinx.coroutines.delay(700)
        isLoading = false
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(bottom = 16.dp)) {

        // --- POPULARES ---
        Text(
            text = "Populares",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        )

        if (isLoading) {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(6) { PeliculaItemPlaceholder() }
            }
        } else {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(populares) { pelicula: PeliculaP ->
                    PeliculaItem(
                        title = pelicula.title,
                        posterPath = pelicula.nivel_path,
                        onPeliculaClick = { onPeliculaClick(pelicula.id, "pelicula") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- PRÓXIMAMENTE ---
        Text(
            text = "Próximamente",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )

        if (isLoading) {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(6) { PeliculaItemPlaceholder() }
            }
        } else {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(proximamente) { pelicula: PeliculaProx ->
                    PeliculaItem(
                        title = pelicula.title,
                        posterPath = pelicula.nivel_path,
                        onPeliculaClick = { onPeliculaClick(pelicula.id, "pelicula") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- MEJOR VALORADAS ---
        Text(
            text = "Mejor Valoradas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )

        if (isLoading) {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(6) { PeliculaItemPlaceholder() }
            }
        } else {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(mejorValoradas) { pelicula: PeliculaValoradas ->
                    PeliculaItem(
                        title = pelicula.title,
                        posterPath = pelicula.nivel_path,
                        onPeliculaClick = { onPeliculaClick(pelicula.id, "pelicula") }
                    )
                }
            }
        }
    }
}
