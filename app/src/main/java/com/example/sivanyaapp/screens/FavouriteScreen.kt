package com.example.sivanyaapp.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sivanyaapp.api.ApiInterface
import com.example.sivanyaapp.api.Product
import com.example.sivanyaapp.api.RetrofitClient
import com.example.sivanyaapp.api.UserEmailRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun FavoriteScreen(navController: NavHostController) {
    var favoriteProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    val context = LocalContext.current

    // Retrieve email from SharedPreferences
    val sharedPreferences = navController.context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    val userEmail = sharedPreferences.getString("email", null)  // Retrieve stored email

    LaunchedEffect(userEmail) {
        if (!userEmail.isNullOrEmpty()) {
            val apiService = RetrofitClient.instance.create(ApiInterface::class.java)
            val request = UserEmailRequest(email = userEmail)  // Pass stored email

            apiService.getFavoriteProducts(request).enqueue(object : Callback<List<Product>> {
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (response.isSuccessful) {
                        favoriteProducts = response.body() ?: emptyList()
                    } else {
                        Toast.makeText(context, "Failed to load favorites", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(context, "User email not found", Toast.LENGTH_SHORT).show()
        }
    }

    // UI
    MainScreen(navController) {
        if (favoriteProducts.isEmpty()) {
            // Show message if no favorite products
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(text = "No favorite products found", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            // Show product grid if there are favorite products
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoriteProducts) { product ->
                    ProductCard(product) {
                        navController.navigate("productDetails/${product.id}") // Fixed error
                    }
                }
            }
        }
    }
}
