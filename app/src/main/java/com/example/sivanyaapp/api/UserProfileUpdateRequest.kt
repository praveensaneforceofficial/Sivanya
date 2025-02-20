package com.example.sivanyaapp.api

data class UserProfileUpdateRequest(
    val full_name: String,
    val email: String,
    val phone: String,
    val address: String
)