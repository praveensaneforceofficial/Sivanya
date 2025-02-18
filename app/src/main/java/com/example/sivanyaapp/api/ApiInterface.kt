package com.example.sivanyaapp.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Define the API Interface for registration
interface ApiInterface {
    @POST("register")
    fun registerUser(@Body user: User): Call<ResponseBody>

    @POST("login")  // Adjust the endpoint as per your backend
    fun loginUser(@Body credentials: LoginCredentials): Call<ResponseBody>
}



