package com.example.sivanyaapp.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current

    fun logoutUser() {
        // Clear login state from SharedPreferences
        val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Clears all stored preferences
        editor.apply()

        // Navigate back to login screen
        navController.navigate("login") {
            popUpTo("home") { inclusive = true } // Removes home screen from back stack
        }
    }

    MainScreen(navController) {  // Keeping the same structure as your Cart screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Profile", fontSize = 32.sp, modifier = Modifier.padding(top = 50.dp))

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { logoutUser() },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 20.dp)
            ) {
                Text(text = "Logout", fontSize = 18.sp)
            }
        }
    }
}
