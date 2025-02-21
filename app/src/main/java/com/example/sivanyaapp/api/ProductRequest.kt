package com.example.sivanyaapp.api

data class ProductRequest(
    val imageUrl: String,
    val name: String,
    val description: String,
    val price: String,
    val quantity: String
)