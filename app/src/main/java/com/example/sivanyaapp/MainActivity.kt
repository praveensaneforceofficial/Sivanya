package com.example.sivanyaapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*
import com.example.sivanyaapp.screens.CartScreen
import com.example.sivanyaapp.screens.LoginScreen
import com.example.sivanyaapp.screens.HomeScreen
import com.example.sivanyaapp.screens.ProductListScreen
import com.example.sivanyaapp.screens.ProfileScreen
import com.example.sivanyaapp.screens.RegisterScreen
import com.example.sivanyaapp.ui.theme.SivanyaAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if the user is logged in
        val sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        setContent {
            SivanyaAPPTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = if (isLoggedIn) "home" else "login"
                ) {
                    composable("login") { LoginScreen(navController) }
                    composable("home") { HomeScreen(navController) }
                    composable("categories") { CartScreen(navController) }
                    composable("cart") { CartScreen(navController) }
                    composable("profile") { ProfileScreen(navController) }
                    composable("products") { ProductListScreen(navController) }
                }

            }
        }
    }
}
