package com.example.sivanyaapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.sivanyaapp.components.CustomBottomBar

@Composable
fun MainScreen(navController: NavHostController, content: @Composable () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFE0F7FA),
        bottomBar = { CustomBottomBar(navController) } // Using our custom bottom bar
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}
