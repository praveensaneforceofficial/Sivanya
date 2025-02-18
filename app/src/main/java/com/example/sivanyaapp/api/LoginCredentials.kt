package com.example.sivanyaapp.api

import com.google.gson.annotations.SerializedName

// Data class to represent login credentials
data class LoginCredentials(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)
