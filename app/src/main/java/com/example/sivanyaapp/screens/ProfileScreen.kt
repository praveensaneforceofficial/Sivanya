package com.example.sivanyaapp.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    val sharedPreferences = navController.context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    val email = sharedPreferences.getString("email", "User")

    fun onLogoutClick() {
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
        navController.navigate("login") {
            popUpTo("profile") { inclusive = true }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Hello, $email", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onLogoutClick() }, modifier = Modifier.fillMaxWidth(0.6f)) {
                Text("Logout")
            }
        }
    }
}

@Preview
@Composable
fun PreviewProfileScreen() {
    ProfileScreen(navController = rememberNavController())
}
