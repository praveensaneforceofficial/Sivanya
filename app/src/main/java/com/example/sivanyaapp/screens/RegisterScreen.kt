package com.example.sivanyaapp.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sivanyaapp.api.ApiInterface
import com.example.sivanyaapp.api.RetrofitClient
import com.example.sivanyaapp.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

@Composable
fun RegisterScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Function to hash the password using SHA-256
    fun hashPassword(password: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashedBytes = digest.digest(password.toByteArray())
            hashedBytes.joinToString("") { "%02x".format(it) }  // Convert bytes to hexadecimal
        } catch (e: Exception) {
            Log.e("RegisterScreen", "Password hashing error: ${e.message}")
            ""
        }
    }

    // Handle the Register Click
    fun onRegisterClick() {
        // Ensure password is not empty before hashing
        if (password.isEmpty()) {
            errorMessage = "Password cannot be empty."
            return
        }

        val passwordHash = hashPassword(password)  // Hash the password before sending

        if (passwordHash.isEmpty()) {
            errorMessage = "Password hashing failed."
            return
        }

        // Create User object
        val user = User(email, phone, fullName, passwordHash)

        // Make API call to register user
        val apiService = RetrofitClient.instance.create(ApiInterface::class.java)
        val call = apiService.registerUser(user)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                try {
                    if (response.isSuccessful) {
                        // Navigate to home screen on successful registration
                        navController.navigate("home")
                    } else {
                        // Log the response message for debugging
                        errorMessage = "Registration failed: ${response.message()}"
                        Log.e("RegisterScreen", "Registration failed: ${response.message()}")
                    }
                } catch (e: Exception) {
                    // Log exception during response handling
                    errorMessage = "Error processing response: ${e.message}"
                    Log.e("RegisterScreen", "Error processing response: ${e.message}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Log network failure
                errorMessage = "Network Error: ${t.message}"
                Log.e("RegisterScreen", "Network Error: ${t.message}")
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register",
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Email Input
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone Input
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Full Name Input
        TextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Register Button
        Button(
            onClick = { onRegisterClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(navController = rememberNavController())
}
