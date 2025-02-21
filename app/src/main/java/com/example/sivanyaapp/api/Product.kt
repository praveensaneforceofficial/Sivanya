package com.example.sivanyaapp.api

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val stock_quantity: Int,
    val image_url: String
)

