package com.example.tp6.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL =
        "https://gestionetudiantsnew-copy-production.up.railway.app/"

    val api: EtudiantApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EtudiantApi::class.java)
    }
}