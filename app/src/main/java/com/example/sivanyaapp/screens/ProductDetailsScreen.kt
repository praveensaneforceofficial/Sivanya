package com.example.sivanyaapp.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.sivanyaapp.api.ApiInterface
import com.example.sivanyaapp.api.FavoriteRequest
import com.example.sivanyaapp.api.Product
import com.example.sivanyaapp.api.ProductIdRequest
import com.example.sivanyaapp.api.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(productId: String, onBack: () -> Unit) {
    var product by remember { mutableStateOf<Product?>(null) }
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var selectedSize by remember { mutableStateOf<String?>(null) }
    var quantity by remember { mutableStateOf(1) }
    val sizes = listOf("S", "M", "L", "XL", "XXL")
    var isFavorite by remember { mutableStateOf(false) }
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

    val apiService = RetrofitClient.instance.create(ApiInterface::class.java)
    val userEmail = sharedPreferences.getString("email", "N/A") ?: "N/A"

    // Fetch product details and favorite status
    LaunchedEffect(productId) {
        apiService.getProductById(ProductIdRequest(productId)).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    product = response.body()
                } else {
                    Toast.makeText(context, "Failed to load product details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Check if the product is already a favorite
        apiService.checkFavoriteStatus(FavoriteRequest(userEmail, productId)).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    isFavorite = true
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Failed to check favorite status", Toast.LENGTH_SHORT).show()
            }
        })
    }

    product?.let { item ->
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFf8e2e7), Color(0xFFf3c6d6))))) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Bar with Back and Favorite Buttons
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }

                    IconButton(onClick = {
                        isFavorite = !isFavorite
                        val request = FavoriteRequest(userEmail, productId)

                        apiService.toggleFavorite(request).enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, if (isFavorite) "Added to Favorites" else "Removed from Favorites", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Failed to update favorite", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }) {
                        Icon(
                            if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Product Image
                Card(
                    modifier = Modifier.fillMaxWidth().height(300.dp).padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(item.image_url),
                        contentDescription = "Product Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Product Info
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(text = item.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "₹${item.price}", fontSize = 20.sp, color = Color(0xFFE91E63), fontWeight = FontWeight.Bold)
                    Text(text = item.description, fontSize = 14.sp, color = Color.Gray)
                }
            }

            // Bottom Floating Button for Add to Cart
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Button(
                    onClick = { showBottomSheet = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add to Cart", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Select Size
                Text("Select Size", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    sizes.forEach { size ->
                        Text(
                            text = size,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedSize == size) Color(0xFF6200EE) else Color.LightGray,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { selectedSize = size }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Select Quantity
                Text("Select Quantity", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "−",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { if (quantity > 1) quantity-- }
                    )

                    Text(
                        text = quantity.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Text(
                        text = "+",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { quantity++ }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // OK & Cancel Buttons
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = { showBottomSheet = false }) { Text("Cancel") }
                    Button(onClick = {
                        if (selectedSize == null) {
                            Toast.makeText(context, "Please select a size!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Added to Cart (Size: $selectedSize, Qty: $quantity)", Toast.LENGTH_SHORT).show()
                            showBottomSheet = false
                        }
                    }) { Text("OK") }
                }
            }
        }
    }

}
