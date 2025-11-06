@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.cristopeliculeitor.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cristopeliculeitor.data.model.Pelicula
import com.example.cristopeliculeitor.data.model.PeliculaDetalles
import com.example.cristopeliculeitor.data.model.Tv
import com.example.cristopeliculeitor.data.model.TvDetalles
import com.example.cristopeliculeitor.viewmodel.Modelo

@Composable
fun DetalleCard(
    itemId: Int,
    itemTipo: String,
    onBackClick: () -> Unit,
    modelo: Modelo
) {
    val pelicula by modelo.detallePelicula.collectAsState()
    val serie by modelo.detalleTv.collectAsState()
    val favoritos by modelo.favoritos.collectAsState()

    LaunchedEffect(itemId, itemTipo) {
        if (itemTipo == "pelicula") modelo.cargarDetallesMovie(itemId)
        else modelo.cargarDetallesTv(itemId)
    }

    val esFavorito = favoritos.any {
        (itemTipo == "pelicula" && it is com.example.cristopeliculeitor.data.model.Pelicula && it.id == itemId) || (itemTipo == "serie" && it is com.example.cristopeliculeitor.data.model.Tv && it.id == itemId)
    }

    Scaffold { padding ->
        val contentModifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())

        Column(modifier = contentModifier) {

            // Imagen principal con los botones superpuestos
            val imageUrl = if (itemTipo == "pelicula")
                pelicula?.backdropPath ?: pelicula?.posterPath
            else
                serie?.backdropPath ?: serie?.posterPath

            val baseUrl = "https://image.tmdb.org/t/p/w500"

            //Contenedor de volver atras y de boton de favoritos
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                AsyncImage(
                    model = baseUrl + (imageUrl ?: ""),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                // Botón de volver (back) de encima de la imagen
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .align(Alignment.TopStart)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                //Boton de favoritos
                val esFavorito by remember(favoritos) {
                    mutableStateOf(
                        favoritos.any {
                            (itemTipo == "pelicula" && it is Pelicula && it.id == itemId) ||
                                    (itemTipo == "serie" && it is Tv && it.id == itemId)
                        }
                    )
                }
                IconButton(
                    onClick = {
                        if (itemTipo == "pelicula" && pelicula != null) {
                            modelo.toggleFavorito(pelicula!!, "pelicula")
                        } else if (itemTipo == "serie" && serie != null) {
                            modelo.toggleFavorito(serie!!, "serie")
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(50.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                ) {
                    Icon(
                        imageVector = if (esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                        tint = if (esFavorito) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }



            // Contenido según tipo de pasado, si es pelicula o serie
            when {
                itemTipo == "pelicula" && pelicula != null ->
                    DetallePeliculaContent(pelicula!!, Modifier)
                itemTipo == "serie" && serie != null ->
                    DetalleSerieContent(serie!!, Modifier)
                else -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

//Detalles de la pelicula titulo, detalle, sipnosis, generos, otros...
@Composable
fun DetallePeliculaContent(detalle: PeliculaDetalles, modifier: Modifier) {
    Column(modifier.padding(16.dp)) {
        Text(detalle.title ?: "", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        detalle.tagline?.takeIf { it.isNotBlank() }?.let {
            Text("«$it»", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
        }

        Spacer(Modifier.height(12.dp))
        Text(detalle.overview ?: "Sin sinopsis disponible", style = MaterialTheme.typography.bodyLarge)

        Spacer(Modifier.height(16.dp))

        SeccionTitulo("Géneros")
        detalle.genres?.takeIf { it.isNotEmpty() }?.let { genres ->
            Row(
                modifier = Modifier.wrapContentSize().padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genres.forEach { genero ->
                    AssistChip(
                        onClick = {},
                        label = { Text(genero.name) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        } ?: Text("Sin géneros disponibles")

        Spacer(Modifier.height(16.dp))

        SeccionTitulo("Detalles")
        DetalleTexto("Duración", "${detalle.runtime ?: 0} min")
        DetalleTexto("Lanzamiento", detalle.releaseDate ?: "Desconocido")
        DetalleTexto("Estado", detalle.status ?: "Desconocido")
        DetalleTexto("Puntuación", "${detalle.voteAverage ?: 0}/10 (${detalle.voteCount ?: 0} votos)")
        DetalleTexto("Presupuesto", "${detalle.budget ?: 0} USD")
        DetalleTexto("Recaudación", "${detalle.revenue ?: 0} USD")
    }
}

//Detalles de la serie nombre, detalle, sipnosis, generos, otros...
@Composable
fun DetalleSerieContent(detalle: TvDetalles, modifier: Modifier) {
    Column(modifier.padding(16.dp)) {
        Text(detalle.name ?: "", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        detalle.tagline?.takeIf { it.isNotBlank() }?.let {
            Text("«$it»", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
        }

        Spacer(Modifier.height(12.dp))
        Text(detalle.overview ?: "Sin sinopsis disponible", style = MaterialTheme.typography.bodyLarge)
        SeccionTitulo("Géneros")
        detalle.genres?.takeIf { it.isNotEmpty() }?.let { genres ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genres.forEach { genero ->
                    AssistChip(
                        onClick = {},
                        label = { Text(genero.name) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        } ?: Text("Sin géneros disponibles")

        Spacer(Modifier.height(16.dp))
        SeccionTitulo("Detalles")
        DetalleTexto("Primera emisión", detalle.firstAirDate ?: "Desconocido")
        DetalleTexto("Temporadas", "${detalle.numberOfSeasons ?: 0}")
        DetalleTexto("Episodios", "${detalle.numberOfEpisodes ?: 0}")
        detalle.episodeRunTime?.firstOrNull()?.let {
            DetalleTexto("Duración promedio", "$it min")
        }
        DetalleTexto("Estado", detalle.status ?: "Desconocido")
        DetalleTexto("Puntuación", "${detalle.voteAverage ?: 0}/10 (${detalle.voteCount ?: 0} votos)")
    }
}


//Tipo de texto con estilo especifico para detalles y genero.
@Composable
private fun SeccionTitulo(titulo: String) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

//Tipo de texto con estilo especifico para los demas.
@Composable
private fun DetalleTexto(label: String, valor: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text("$label:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Text(valor, style = MaterialTheme.typography.bodyMedium)
    }
}
