package com.example.sivanyaapp.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Define the API Interface for registration
interface ApiInterface {

    @POST("register")  // Use the appropriate endpoint
    fun registerUser(@Body user: User): Call<User>
}
