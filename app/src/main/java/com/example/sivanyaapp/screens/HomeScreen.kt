package com.example.sivanyaapp.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current  // ✅ Correct way to get context

    fun onLogoutClick() {
        val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
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

            // Bottom Navigation Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Home Icon
                        IconButton(onClick = { navController.navigate("home") }) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(32.dp),
                                tint = Color(0xFFB196E5)
                            )
                        }

                        // Category Icon
                        IconButton(onClick = { navController.navigate("categories") }) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Categories",
                                modifier = Modifier.size(32.dp),
                                tint = Color(0xFFB39DDB)
                            )
                        }

                        // Cart Icon
                        IconButton(onClick = { navController.navigate("cart") }) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "Cart",
                                modifier = Modifier.size(32.dp),
                                tint = Color(0xFFB39DDB)
                            )
                        }

                        // Profile Icon
                        IconButton(onClick = { navController.navigate("profile") }) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(32.dp),
                                tint = Color(0xFFB39DDB)
                            )
                        }
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
