package com.example.sivanyaapp.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.layout.Arrangement  // Add this import


@Composable
fun HomeScreen(navController: NavHostController) {
    // Function to handle logout logic
    fun onLogoutClick() {
        val sharedPreferences = navController.context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)  // Mark as logged out
        editor.apply()

        // Navigate to login screen
        navController.navigate("login") {
            popUpTo("home") { inclusive = true }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFE0F7FA)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome to SivanyaApp!",
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Button
                Button(
                    onClick = { onLogoutClick() },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Logout")
                }
            }

            // Bottom Row with multiple icons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp) // Adjust the padding as needed
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp) // Ensure it is at the bottom
                ) {
                    // Home Icon
                    IconButton(onClick = { /* Handle Home icon click */ }) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home Icon",
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF00796B)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    // Category Icon
                    IconButton(onClick = { /* Handle Category icon click */ }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite, // Use the favorite icon for category
                            contentDescription = "Favorite Icon",
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF00796B)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    // Cart Icon
                    IconButton(onClick = { /* Handle Cart icon click */ }) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Cart Icon",
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF00796B)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    // Profile Icon
                    IconButton(onClick = { /* Handle Profile icon click */ }) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile Icon",
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF00796B)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(navController = rememberNavController())
}
