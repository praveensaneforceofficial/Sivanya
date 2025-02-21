package com.example.sivanyaapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import com.example.sivanyaapp.api.Product

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp) // Smaller corner radius
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            // **Product Image (Fixed 20mm x 20mm = 76dp x 76dp)**
            Image(
                painter = rememberAsyncImagePainter(product.image_url),
                contentDescription = "Product Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(76.dp) // Fixed Image Size
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // **Product Name (Smaller Font)**
            Text(
                text = product.name,
                fontSize = 12.sp, // Smaller font size
                fontWeight = FontWeight.Bold
            )

            // **Product Description (Smaller Font)**
            Text(
                text = product.description,
                fontSize = 10.sp, // Smaller font size
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            // **Price & Buy Button (Fixed 10mm = 38dp)**
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp), // Fixed bottom section height
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // **Price Text**
                Text(
                    text = "₹${product.price}",
                    fontSize = 12.sp, // Smaller font size
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE)
                )

                // **Buy Button**
                Button(
                    onClick = { /* Handle Click */ },
                    modifier = Modifier.height(38.dp) // Fixed button height
                ) {
                    Text(text = "Buy", fontSize = 10.sp) // Smaller button text
                }
            }
        }
    }
}
