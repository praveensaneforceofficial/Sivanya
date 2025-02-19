package com.example.sivanyaapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

val categories = listOf("Clothing", "Electronics", "Footwear", "Accessories")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Categories") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            categories.forEach { category ->
                Text(
                    text = category,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { navController.navigate("products") }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCategoryScreen() {
    CategoryScreen(navController = rememberNavController())
}
