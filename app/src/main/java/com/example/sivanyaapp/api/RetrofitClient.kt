package com.example.sivanyaapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object RetrofitClient {

    // Load properties from gradle.properties
    private val properties: Properties = Properties().apply {
        load(javaClass.classLoader?.getResourceAsStream("gradle.properties"))
    }

    // Access BASE_URL directly from gradle.properties
    private val BASE_URL = properties.getProperty("BASE_URL", "https://default.url") // Use default URL if not found

    // Initialize Retrofit client
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)  // Use the loaded BASE_URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
