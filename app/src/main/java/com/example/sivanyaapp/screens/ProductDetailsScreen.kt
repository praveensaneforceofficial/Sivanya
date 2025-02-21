package com.example.sivanyaapp.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.sivanyaapp.api.ApiInterface
import com.example.sivanyaapp.api.Product
import com.example.sivanyaapp.api.ProductIdRequest
import com.example.sivanyaapp.api.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(productId: String) {
    var product by remember { mutableStateOf<Product?>(null) }
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var selectedSize by remember { mutableStateOf<String?>(null) }
    var quantity by remember { mutableStateOf(1) }
    val sizes = listOf("S", "M", "L", "XL", "XXL")

    // Fetch product details
    LaunchedEffect(productId) {
        val apiService = RetrofitClient.instance.create(ApiInterface::class.java)
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
    }

    product?.let { item ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)) // Light Gray Background
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Product Image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
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
            Text(
                text = item.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = "₹${item.price}",
                fontSize = 20.sp,
                color = Color(0xFFE91E63),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Description
            Text(
                text = item.description,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Buttons: Add to Cart & Favorite
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { showBottomSheet = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                    modifier = Modifier.weight(1f).padding(8.dp)
                ) {
                    Text("Add to Cart", color = Color.White, fontSize = 16.sp)
                }

                Button(
                    onClick = {
                        Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                    modifier = Modifier.weight(1f).padding(8.dp)
                ) {
                    Text("❤ Add to Favorite", color = Color.White, fontSize = 16.sp)
                }
            }
        }

        // Bottom Sheet for Size & Quantity Selection
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Select Size", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Size Selection (Stylish Buttons)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        sizes.forEach { size ->
                            Button(
                                onClick = { selectedSize = size },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedSize == size) Color(0xFF6200EE) else Color.Gray
                                ),
                                modifier = Modifier.padding(4.dp),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(size, color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quantity Selector
                    Text("Select Quantity", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { if (quantity > 1) quantity-- },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text("-", fontSize = 20.sp, color = Color.White)
                        }

                        Text(
                            text = quantity.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Button(
                            onClick = { quantity++ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text("+", fontSize = 20.sp, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Confirm Button (Validates Size Selection)
                    Button(
                        onClick = {
                            if (selectedSize == null) {
                                Toast.makeText(context, "Please select a size!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Added to Cart (Size: $selectedSize, Qty: $quantity)", Toast.LENGTH_SHORT).show()
                                showBottomSheet = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Confirm", color = Color.White, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
