package com.example.cristopeliculeitor.data.model

import com.google.gson.annotations.SerializedName

// Array list películas
data class PeliculaRes(
    val page: Int,
    val results: List<Pelicula>,
    @SerializedName("total_pages") val total_paginas: Int
)

data class Pelicula(
    val id: Int,
    val title: String,
    val original_titulo: String?,
    val fecha_actual: String?,
    @SerializedName("poster_path") val nivel_path: String?, // ✅ se enlaza correctamente al JSON
    val overview: String
)


// Array list películas populares
data class PeliculaResP(
    val page: Int,
    val results: List<PeliculaP>,
    @SerializedName("total_pages") val total_paginas: Int
)

data class PeliculaP(
    val id: Int,
    val title: String,
    val original_titulo: String?,
    val fecha_actual: String?,
    @SerializedName("poster_path") val nivel_path: String?, // ✅ igual aquí
    val overview: String
)


// Array list películas próximamente
data class PeliculaResProx(
    val page: Int,
    val results: List<PeliculaProx>,
    @SerializedName("total_pages") val total_paginas: Int
)

data class PeliculaProx(
    val id: Int,
    val title: String,
    val original_titulo: String?,
    val fecha_actual: String?,
    @SerializedName("poster_path") val nivel_path: String?, // ✅
    val overview: String
)


// Array list películas mejor valoradas
data class PeliculaResValoradas(
    val page: Int,
    val results: List<PeliculaValoradas>,
    @SerializedName("total_pages") val total_paginas: Int
)

data class PeliculaValoradas(
    val id: Int,
    val title: String,
    val original_titulo: String?,
    val fecha_actual: String?,
    @SerializedName("poster_path") val nivel_path: String?, // ✅
    val overview: String
)


// Array list detalles de película

data class PeliculaDetalles(
    val id: Int,
    val title: String?,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    val budget: Int?,
    val revenue: Int?,
    val status: String?,
    val tagline: String?,
    val popularity: Float?,
    @SerializedName("vote_average") val voteAverage: Float?,
    @SerializedName("vote_count") val voteCount: Int?,
    val genres: List<Genero>?,
    @SerializedName("production_companies") val productionCompanies: List<Productora>?,
    @SerializedName("production_countries") val productionCountries: List<Pais>?,
    @SerializedName("spoken_languages") val spokenLanguages: List<Idioma>?
)

data class Genero(val id: Int, val name: String)
data class Productora(
    val id: Int,
    val name: String?,
    @SerializedName("origin_country") val originCountry: String?
)
data class Pais(
    @SerializedName("iso_3166_1") val code: String?,
    val name: String?
)
data class Idioma(
    @SerializedName("english_name") val englishName: String?
)
data class PeliculaResBusqueda(
    val page: Int,
    @SerializedName("results") val results: List<PeliculaBusqueda>,
    @SerializedName("total_pages") val totalPaginas: Int
)

data class PeliculaBusqueda(
    val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("poster_path") val posterPath: String?,
    val overview: String?
)