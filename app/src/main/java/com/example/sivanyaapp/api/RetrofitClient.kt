package com.example.sivanyaapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

object RetrofitClient {

    const val BASE_URL = "https://sivanyaapi.onrender.com"  // Your base URL

    // Initialize Retrofit client with logging
    val instance: Retrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)  // Use the loaded BASE_URL
            .client(client)  // Add logging client
            .addConverterFactory(GsonConverterFactory.create())  // Gson for JSON parsing if needed
            .build()
    }
}
