package com.example.cristopeliculeitor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cristopeliculeitor.data.model.*
import com.example.cristopeliculeitor.data.remote.RetrofitCliente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Modelo : ViewModel() {

    private val apiKey = "67ecb1a2aaf8123195a0f30fe9587324"
    private val language = "es-ES"
    private val api = RetrofitCliente.api

    // ----------------- Flujos de datos -----------------
    private val _peliculas = MutableStateFlow<List<Pelicula>>(emptyList())
    val peliculas = _peliculas.asStateFlow()

    private val _peliculaPopular = MutableStateFlow<List<PeliculaP>>(emptyList())
    val peliculaPopular = _peliculaPopular.asStateFlow()

    private val _peliculaProximamente = MutableStateFlow<List<PeliculaProx>>(emptyList())
    val peliculaProximamente = _peliculaProximamente.asStateFlow()

    private val _peliculaMejorValorada = MutableStateFlow<List<PeliculaValoradas>>(emptyList())
    val peliculaMejorValorada = _peliculaMejorValorada.asStateFlow()

    private val _series = MutableStateFlow<List<Tv>>(emptyList())
    val series = _series.asStateFlow()

    private val _seriePopular = MutableStateFlow<List<TvP>>(emptyList())
    val seriePopular = _seriePopular.asStateFlow()

    private val _serieEnEmision = MutableStateFlow<List<TvEnEmision>>(emptyList())
    val serieEnEmision = _serieEnEmision.asStateFlow()

    private val _serieMejorValorada = MutableStateFlow<List<TvValoradas>>(emptyList())
    val serieMejorValorada = _serieMejorValorada.asStateFlow()

    private val _favoritos = MutableStateFlow<List<Any>>(emptyList())
    val favoritos = _favoritos.asStateFlow()

    private val _detallePelicula = MutableStateFlow<PeliculaDetalles?>(null)
    val detallePelicula = _detallePelicula.asStateFlow()

    private val _detalleTv = MutableStateFlow<TvDetalles?>(null)
    val detalleTv = _detalleTv.asStateFlow()

    // 🔍 Resultados de búsqueda
    private val _resultadosBusquedaMovie = MutableStateFlow<List<PeliculaBusqueda>>(emptyList())
    val resultadosBusquedaMovie: StateFlow<List<PeliculaBusqueda>> = _resultadosBusquedaMovie

    private val _resultadosBusquedaTv = MutableStateFlow<List<TvBusqueda>>(emptyList())
    val resultadosBusquedaTv: StateFlow<List<TvBusqueda>> = _resultadosBusquedaTv

    // ----------------- Películas -----------------
    fun cargarPeliculaPopular() {
        viewModelScope.launch {
            try {
                val lista = mutableListOf<PeliculaP>()
                var page = 1
                var totalPaginas: Int
                do {
                    val response = api.getMoviesPopular(apiKey, language, page)
                    totalPaginas = response.total_paginas
                    lista.addAll(response.results)
                    page++
                } while (page <= totalPaginas && page <= 2)
                _peliculaPopular.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cargarPeliculaProximamente() {
        viewModelScope.launch {
            try {
                val lista = mutableListOf<PeliculaProx>()
                var page = 1
                var totalPaginas: Int
                do {
                    val response = api.getMoviesProximamente(apiKey, language, page)
                    totalPaginas = response.total_paginas
                    lista.addAll(response.results)
                    page++
                } while (page <= totalPaginas && page <= 2)
                _peliculaProximamente.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cargarPeliculaMejorValorada() {
        viewModelScope.launch {
            try {
                val lista = mutableListOf<PeliculaValoradas>()
                var page = 1
                var totalPaginas: Int
                do {
                    val response = api.getMoviesTopRated(apiKey, language, page)
                    totalPaginas = response.total_paginas
                    lista.addAll(response.results)
                    page++
                } while (page <= totalPaginas && page <= 2)
                _peliculaMejorValorada.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ----------------- Series -----------------
    fun cargarSeriePopular() {
        viewModelScope.launch {
            try {
                val lista = mutableListOf<TvP>()
                var page = 1
                var totalPaginas: Int
                do {
                    val response = api.getTvPopular(apiKey, language, page)
                    totalPaginas = response.totalPaginas
                    lista.addAll(response.results)
                    page++
                } while (page <= totalPaginas && page <= 2)
                _seriePopular.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cargarSerieEnEmision() {
        viewModelScope.launch {
            try {
                val lista = mutableListOf<TvEnEmision>()
                var page = 1
                var totalPaginas: Int
                do {
                    val response = api.getTvEnEmision(apiKey, language, page)
                    totalPaginas = response.totalPaginas
                    lista.addAll(response.results)
                    page++
                } while (page <= totalPaginas && page <= 2)
                _serieEnEmision.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cargarSerieMejorValorada() {
        viewModelScope.launch {
            try {
                val lista = mutableListOf<TvValoradas>()
                var page = 1
                var totalPaginas: Int
                do {
                    val response = api.getTvTopRated(apiKey, language, page)
                    totalPaginas = response.totalPaginas
                    lista.addAll(response.results)
                    page++
                } while (page <= totalPaginas && page <= 2)
                _serieMejorValorada.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ----------------- Detalles -----------------
    fun cargarDetallesMovie(id: Int) {
        viewModelScope.launch {
            try {
                _detallePelicula.value = api.getMovieDetalles(id, apiKey, language)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cargarDetallesTv(id: Int) {
        viewModelScope.launch {
            try {
                _detalleTv.value = api.getTvDetalles(id, apiKey, language)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ----------------- Favoritos -----------------
    fun toggleFavorito(item: Any, tipo: String) {
        val id = when (item) {
            is Pelicula -> item.id
            is PeliculaP -> item.id
            is PeliculaProx -> item.id
            is PeliculaValoradas -> item.id
            is PeliculaDetalles -> item.id ?: 0
            is Tv -> item.id
            is TvP -> item.id
            is TvEnEmision -> item.id
            is TvValoradas -> item.id
            is TvDetalles -> item.id ?: 0
            else -> return
        }

        val esFavorito = _favoritos.value.any {
            (it as? Pelicula)?.id == id || (it as? Tv)?.id == id
        }

        _favoritos.value = if (esFavorito) {
            _favoritos.value.filterNot {
                (it as? Pelicula)?.id == id || (it as? Tv)?.id == id
            }
        } else {
            val nuevo = when (item) {
                is Pelicula, is PeliculaP, is PeliculaProx, is PeliculaValoradas -> {
                    val p = item as Pelicula
                    Pelicula(
                        id = p.id,
                        title = p.title,
                        original_titulo = p.original_titulo ?: p.title,
                        fecha_actual = p.fecha_actual ?: "",
                        nivel_path = p.nivel_path ?: "",
                        overview = p.overview ?: ""
                    )
                }
                is PeliculaDetalles -> Pelicula(
                    id = item.id ?: 0,
                    title = item.title ?: "Sin título",
                    original_titulo = item.originalTitle ?: item.title ?: "Sin título",
                    fecha_actual = item.releaseDate ?: "",
                    nivel_path = item.posterPath ?: item.backdropPath ?: "",
                    overview = item.overview ?: ""
                )
                is Tv, is TvP, is TvEnEmision, is TvValoradas -> {
                    val s = item as Tv
                    Tv(
                        id = s.id,
                        name = s.name,
                        originalName = s.originalName ?: s.name,
                        firstAirDate = s.firstAirDate ?: "",
                        posterPath = s.posterPath ?: "",
                        overview = s.overview ?: ""
                    )
                }
                is TvDetalles -> Tv(
                    id = item.id ?: 0,
                    name = item.name ?: "Sin título",
                    originalName = item.originalName ?: item.name ?: "Sin título",
                    firstAirDate = item.firstAirDate ?: "",
                    posterPath = item.posterPath ?: item.backdropPath ?: "",
                    overview = item.overview ?: ""
                )
                else -> return
            }
            _favoritos.value + nuevo
        }
    }



    // ----------------- Búsqueda -----------------
    fun buscarPeliculas(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _resultadosBusquedaMovie.value = emptyList()
            } else {
                try {
                    val respuesta = api.getbBuscarPeliculas(query, apiKey, language)
                    _resultadosBusquedaMovie.value = respuesta.results
                } catch (e: Exception) {
                    e.printStackTrace()
                    _resultadosBusquedaMovie.value = emptyList()
                }
            }
        }
    }

    fun buscarSeries(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _resultadosBusquedaTv.value = emptyList()
            } else {
                try {
                    val respuesta = api.getBuscarTv(query, apiKey, language)
                    _resultadosBusquedaTv.value = respuesta.resultsTv
                } catch (e: Exception) {
                    e.printStackTrace()
                    _resultadosBusquedaTv.value = emptyList()
                }
            }
        }
    }
}
