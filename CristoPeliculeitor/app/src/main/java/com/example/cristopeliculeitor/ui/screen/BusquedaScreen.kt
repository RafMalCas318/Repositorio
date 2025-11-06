package com.example.cristopeliculeitor.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cristopeliculeitor.data.model.PeliculaBusqueda
import com.example.cristopeliculeitor.data.model.TvBusqueda
import com.example.cristopeliculeitor.viewmodel.Modelo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaScreen(
    modelo: Modelo,
    onBackClick: () -> Unit,
    onItemClick: (Int, String) -> Unit // (id, tipo: "pelicula" o "serie")
) {
    var query by remember { mutableStateOf("") }

    val peliculas by modelo.resultadosBusquedaMovie.collectAsState()
    val series by modelo.resultadosBusquedaTv.collectAsState()
    val scope = rememberCoroutineScope()

    // Combinar películas + series en una sola lista
    val resultados = remember(peliculas, series) {
        val lista = mutableListOf<Pair<String, Any>>()
        lista.addAll(peliculas.map { "pelicula" to it })
        lista.addAll(series.map { "serie" to it })
        lista
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TextField(
                        value = query,
                        onValueChange = {
                            query = it
                            scope.launch {
                                modelo.buscarPeliculas(query)
                                modelo.buscarSeries(query)
                            }
                        },
                        placeholder = { Text("Buscar películas o series...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            if (query.isNotEmpty() && resultados.isEmpty()) {
                item {
                    Text(
                        text = "Sin resultados para \"$query\"",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(resultados) { (tipo, item) ->
                    when (tipo) {
                        "pelicula" -> ResultadoPeliculaItem(item as PeliculaBusqueda, onItemClick)
                        "serie" -> ResultadoSerieItem(item as TvBusqueda, onItemClick)
                    }
                }
            }
        }
    }
}

@Composable
fun ResultadoPeliculaItem(item: PeliculaBusqueda, onClick: (Int, String) -> Unit) {
    val baseUrl = "https://image.tmdb.org/t/p/w500"
    val poster = item.posterPath?.let { "$baseUrl$it" } ?: ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick(item.id, "pelicula") },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = poster,
                contentDescription = item.title,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = item.title ?: "Sin título",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = item.overview ?: "Sin descripción",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3
                )
            }
        }
    }
}

@Composable
fun ResultadoSerieItem(item: TvBusqueda, onClick: (Int, String) -> Unit) {
    val baseUrl = "https://image.tmdb.org/t/p/w500"
    val poster = item.posterPath?.let { "$baseUrl$it" } ?: ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick(item.id, "serie") },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = poster,
                contentDescription = item.name,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = item.name ?: "Sin título",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = item.overview ?: "Sin descripción",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3
                )
            }
        }
    }
}
