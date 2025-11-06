// Archivo: ApiService.kt

package com.example.cristopeliculeitor.data.remote

import androidx.compose.ui.input.key.Key
import com.example.cristopeliculeitor.data.model.PeliculaRes
import com.example.cristopeliculeitor.data.model.TvRes
import com.example.cristopeliculeitor.data.model.PeliculaResP
import com.example.cristopeliculeitor.data.model.TvResP
import com.example.cristopeliculeitor.data.model.PeliculaResProx
import com.example.cristopeliculeitor.data.model.TvResEnEmision
import com.example.cristopeliculeitor.data.model.PeliculaResValoradas
import com.example.cristopeliculeitor.data.model.TvResValoradas
import com.example.cristopeliculeitor.data.model.PeliculaDetalles
import com.example.cristopeliculeitor.data.model.TvDetalles
import com.example.cristopeliculeitor.data.model.TvResBusqueda
import com.example.cristopeliculeitor.data.model.PeliculaResBusqueda



import org.intellij.lang.annotations.Language
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int = 1,
        @QueryMap filters: Map<String, String> = emptyMap()
    ): PeliculaRes


    @GET("discover/movie")
    suspend fun getTv(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int = 1,
        @QueryMap filters: Map<String, String> = emptyMap()
    ): TvRes

    @GET("movie/popular")
    suspend fun getMoviesPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int = 1,
        @QueryMap filters: Map<String, String> = emptyMap()
    ): PeliculaResP

    @GET("tv/popular")
    suspend fun getTvPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int = 1,
        @QueryMap filters: Map<String, String> = emptyMap()
    ): TvResP

    @GET("movie/upcoming")
    suspend fun getMoviesProximamente(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int = 1,
        @QueryMap filters: Map<String, String> = emptyMap()
    ): PeliculaResProx

    // Nueva función para Películas Mejor Valoradas
    @GET("movie/top_rated")
    suspend fun getMoviesTopRated(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int = 1,
        @QueryMap filters: Map<String, String> = emptyMap()
    ): PeliculaResValoradas // Usar la clase de respuesta existente en Pelicula.kt

    @GET("tv/on_the_air")
    suspend fun getTvEnEmision(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int = 1,
        @QueryMap filters: Map<String, String> = emptyMap()
    ): TvResEnEmision

    // Nueva función para Series Mejor Valoradas
    @GET("tv/top_rated")
    suspend fun getTvTopRated(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int = 1,
        @QueryMap filters: Map<String, String> = emptyMap()
    ): TvResValoradas
    @GET("movie/{id}")
    suspend fun getMovieDetalles(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): PeliculaDetalles

    @GET("tv/{id}")
    suspend fun getTvDetalles(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): TvDetalles

    @GET("search/movie")
    suspend fun getbBuscarPeliculas(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = "TU_API_KEY",
        @Query("language") language: String?
    ): PeliculaResBusqueda

    @GET("search/tv")
    suspend fun getBuscarTv(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = "TU_API_KEY",
        @Query("language") language: String?
    ): TvResBusqueda

}