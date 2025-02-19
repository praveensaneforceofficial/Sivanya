package com.example.sivanyaapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter

data class Product(val name: String, val price: String, val imageUrl: String)

val sampleProducts = listOf(
    Product("T-shirt", "$10", "https://via.placeholder.com/150"),
    Product("Jeans", "$20", "https://via.placeholder.com/150"),
    Product("Shoes", "$30", "https://via.placeholder.com/150")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Products") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(sampleProducts) { product ->
                    ProductItem(product)
                }
            }
        }
    }
}


@Composable
fun ProductItem(product: Product) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Navigate to Product Details */ },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberImagePainter(product.imageUrl),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(product.name, fontSize = 20.sp)
                Text(product.price, fontSize = 16.sp, color = Color.Gray)
            }
        }
    }
}

@Preview
@Composable
fun PreviewProductListScreen() {
    ProductListScreen(navController = rememberNavController())
}
