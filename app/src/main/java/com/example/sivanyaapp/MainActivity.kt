package com.example.sivanyaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController  // <-- Add this import
import androidx.navigation.compose.*
import com.example.sivanyaapp.screens.LoginScreen
import com.example.sivanyaapp.screens.HomeScreen
import com.example.sivanyaapp.screens.ForgetPasswordScreen
import com.example.sivanyaapp.screens.RegisterScreen
import com.example.sivanyaapp.ui.theme.SivanyaAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SivanyaAPPTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(navController)
                    }
                    composable("home") {
                        HomeScreen()
                    }
                    composable("forgetPassword") {
                        ForgetPasswordScreen(navController)
                    }
                    composable("register") {
                        RegisterScreen(navController)
                    }
                }
            }
        }
    }
}
