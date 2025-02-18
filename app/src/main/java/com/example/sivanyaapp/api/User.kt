package com.example.sivanyaapp.api

import com.google.gson.annotations.SerializedName

// Data class to represent user details
data class User(
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("password") val passwordHash: String
)
