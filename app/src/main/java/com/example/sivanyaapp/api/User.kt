package com.example.sivanyaapp.api

import com.google.gson.annotations.SerializedName

// Data class to represent user details
data class User(
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("password_hash") val passwordHash: String
)
