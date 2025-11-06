package com.example.cristopeliculeitor.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cristopeliculeitor.data.model.*
import com.example.cristopeliculeitor.viewmodel.Modelo


data class ItemInfo(
    val title: String?,
    val posterPath: String?,
    val id: Int,
    val tipo: String
)

@Composable
fun FavoritosScreen(
    modelo: Modelo = viewModel(),
    onItemClick: (Int, String) -> Unit
) {
    val favoritos by modelo.favoritos.collectAsState()
    var isLoading by remember { mutableStateOf(true) }

    // Simula una pequeña espera de carga para suavizar la transición visual
    LaunchedEffect(favoritos) {
        if (favoritos.isEmpty()) {
            isLoading = true
            kotlinx.coroutines.delay(500) // medio segundo de espera
        }
        isLoading = false
    }

    // Contenedor principal
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                // 🔹 Placeholder de carga tipo shimmer
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(6) { // 6 placeholders simulando tarjetas
                        FavoritoCardPlaceholder()
                    }
                }
            }

            favoritos.isEmpty() -> {
                // 🔹 Mensaje si no hay favoritos
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aún no tienes favoritos...",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                // 🔹 Grid de favoritos reales
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(favoritos) { item ->
                        val (title, posterPath, id, tipo) = when (item) {
                            is Pelicula -> ItemInfo(item.title, item.nivel_path, item.id, "pelicula")
                            is PeliculaP -> ItemInfo(item.title, item.nivel_path, item.id, "pelicula")
                            is PeliculaProx -> ItemInfo(item.title, item.nivel_path, item.id, "pelicula")
                            is PeliculaValoradas -> ItemInfo(item.title, item.nivel_path, item.id, "pelicula")
                            is Tv -> ItemInfo(item.name, item.posterPath, item.id, "serie")
                            is TvP -> ItemInfo(item.name, item.posterPath, item.id, "serie")
                            is TvEnEmision -> ItemInfo(item.name, item.posterPath, item.id, "serie")
                            is TvValoradas -> ItemInfo(item.name, item.posterPath, item.id, "serie")
                            else -> ItemInfo("Desconocido", null, 0, "")
                        }

                        val fullImageUrl = if (!posterPath.isNullOrEmpty()) {
                            "https://image.tmdb.org/t/p/w500$posterPath"
                        } else {
                            "https://via.placeholder.com/500x750?text=No+Image"
                        }

                        FavoritoCard(
                            title = title ?: "Sin título",
                            imageUrl = fullImageUrl,
                            onClick = { onItemClick(id, tipo) }
                        )
                    }
                }
            }
        }
    }
}

// 🔸 Composable del card de favorito real
@Composable
fun FavoritoCard(
    title: String,
    imageUrl: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(260.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp)
            )
        }
    }
}

// 🔸 Placeholder (efecto shimmer)
@Composable
fun FavoritoCardPlaceholder() {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(260.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {}
}
