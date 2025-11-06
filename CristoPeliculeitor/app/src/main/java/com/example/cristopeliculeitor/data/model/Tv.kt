package com.example.cristopeliculeitor.data.model

import com.google.gson.annotations.SerializedName

// 🔹 Respuesta general para series (TV)
data class TvRes(
    val page: Int,
    @SerializedName("results") val results: List<Tv>, // lista de series
    @SerializedName("total_pages") val totalPaginas: Int
)

// 🔹 Modelo base para cada serie
data class Tv(
    val id: Int,
    @SerializedName("name") val name: String?,              // nombre de la serie
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("poster_path") val posterPath: String?, // ruta del póster
    val overview: String?
)


// 🔹 Series Populares
data class TvResP(
    val page: Int,
    @SerializedName("results") val results: List<TvP>,
    @SerializedName("total_pages") val totalPaginas: Int
)

data class TvP(
    val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("poster_path") val posterPath: String?,
    val overview: String?
)


// 🔹 Series En Emisión
data class TvResEnEmision(
    val page: Int,
    @SerializedName("results") val results: List<TvEnEmision>,
    @SerializedName("total_pages") val totalPaginas: Int
)

data class TvEnEmision(
    val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("poster_path") val posterPath: String?,
    val overview: String?
)


// 🔹 Series Mejor Valoradas
data class TvResValoradas(
    val page: Int,
    @SerializedName("results") val results: List<TvValoradas>,
    @SerializedName("total_pages") val totalPaginas: Int
)

data class TvValoradas(
    val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("poster_path") val posterPath: String?,
    val overview: String?
)


// 🔹 Detalles de una serie
data class TvDetalles(
    val id: Int,
    val name: String?,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    val status: String?,
    val tagline: String?,
    val popularity: Float?,
    @SerializedName("vote_average") val voteAverage: Float?,
    @SerializedName("vote_count") val voteCount: Int?,
    val genres: List<Genero>?,
    @SerializedName("production_companies") val productionCompanies: List<Productora>?,
    @SerializedName("production_countries") val productionCountries: List<Pais>?,
    @SerializedName("spoken_languages") val spokenLanguages: List<Idioma>?,
    @SerializedName("number_of_seasons") val numberOfSeasons: Int?,
    @SerializedName("number_of_episodes") val numberOfEpisodes: Int?,
    @SerializedName("episode_run_time") val episodeRunTime: List<Int>?
)

data class TvResBusqueda(
    val page: Int,
    @SerializedName("results") val resultsTv: List<TvBusqueda>,
    @SerializedName("total_pages") val totalPaginas: Int
)

data class TvBusqueda(
    val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("poster_path") val posterPath: String?,
    val overview: String?
)