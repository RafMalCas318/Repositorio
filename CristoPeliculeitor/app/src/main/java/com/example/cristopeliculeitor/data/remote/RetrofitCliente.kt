package com.example.cristopeliculeitor.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.example.cristopeliculeitor.data.remote.ApiService
object RetrofitCliente {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}
