package com.example.sivanyaapp.screens

import android.content.Context
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
import com.example.sivanyaapp.api.LoginCredentials
import com.example.sivanyaapp.api.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun LoginScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) } // State to manage loader visibility

    // Function to handle login click
    fun onLoginClick() {
        // Ensure that username and password are not empty
        if (username.isEmpty() || password.isEmpty()) {
            errorMessage = "Username and password cannot be empty."
            return
        }

        // Set loading state to true to show loader
        loading = true

        val credentials = LoginCredentials(email = username, password = password)

        // Make the API call to login
        val apiService = RetrofitClient.instance.create(ApiInterface::class.java)
        val call = apiService.loginUser(credentials)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                loading = false // Hide loader once response is received
                try {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.string()
                        if (responseBody != null && responseBody.contains("User logged in successfully")) {
                            // Save login state to SharedPreferences
                            val sharedPreferences = navController.context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("isLoggedIn", true)  // Mark as logged in
                            editor.putString("email", username)
                            editor.apply()

                            // Navigate to home screen on successful login
                            navController.navigate("home")
                        } else {
                            errorMessage = "Invalid username or password."
                            Log.e("LoginScreen", "Login failed: $responseBody")
                        }
                    } else {
                        // Log the response message for debugging
                        errorMessage = "Login failed: ${response.message()}"
                        Log.e("LoginScreen", "Login failed: ${response.message()}")
                    }
                } catch (e: Exception) {
                    errorMessage = "Error processing response: ${e.message}"
                    Log.e("LoginScreen", "Error processing response: ${e.message}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loading = false // Hide loader on network failure
                // Log network failure
                errorMessage = "Network Error: ${t.message}"
                Log.e("LoginScreen", "Network Error: ${t.message}")
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
            text = "Login",
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Username (Email) Input
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username (Email)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),  // Password should be masked
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Show loader when loading state is true
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        // Login Button
        Button(
            onClick = { onLoginClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Forgot Password Button
        TextButton(onClick = { navController.navigate("forgetPassword") }) {
            Text("Forgot Password?")
        }

        // Register Button
        TextButton(onClick = { navController.navigate("register") }) {
            Text("Register")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = rememberNavController())
}
