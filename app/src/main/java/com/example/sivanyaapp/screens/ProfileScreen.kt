package com.example.sivanyaapp.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sivanyaapp.api.ApiInterface
import com.example.sivanyaapp.api.RetrofitClient
import com.example.sivanyaapp.api.UserEmailRequest
import com.example.sivanyaapp.api.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        fetchUserDetails(context) { user, error ->
            if (error != null) {
                errorMessage = error
            } else {
                fullName = user["full_name"] ?: ""
                email = user["email"] ?: ""
                phone = user["phone"] ?: ""
                address = user["address"] ?: ""
            }
            loading = false
        }
    }

    fun logoutUser() {
        val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        navController.navigate("login") {
            popUpTo("home") { inclusive = true }
        }
    }

    MainScreen(navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Profile", fontSize = 32.sp, modifier = Modifier.padding(top = 50.dp))

            Spacer(modifier = Modifier.height(20.dp))

            if (loading) {
                CircularProgressIndicator()
            } else if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = androidx.compose.ui.graphics.Color.Red)
            } else {
                Text(text = "Full Name: $fullName", fontSize = 20.sp)
                Text(text = "Email: $email", fontSize = 20.sp)
                Text(text = "Phone: $phone", fontSize = 20.sp)
                Text(text = "Address: $address", fontSize = 20.sp)
            }

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

fun fetchUserDetails(context: Context, onResult: (Map<String, String>, String?) -> Unit) {
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    val userEmail = sharedPreferences.getString("email", null)

    if (userEmail == null) {
        onResult(emptyMap(), "User email not found. Please log in again.")
        return
    }

    val apiService = RetrofitClient.instance.create(ApiInterface::class.java)
    val call = apiService.getUserProfile(UserEmailRequest(userEmail))

    call.enqueue(object : Callback<UserProfileResponse> {
        override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    onResult(
                        mapOf(
                            "full_name" to (user.full_name ?: "N/A"),
                            "email" to (user.email ?: "N/A"),
                            "phone" to (user.phone ?: "N/A"),
                            "address" to (user.address ?: "N/A")
                        ), null
                    )
                } else {
                    onResult(emptyMap(), "Failed to parse user data.")
                }
            } else {
                onResult(emptyMap(), "Error: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
            Log.e("ProfileScreen", "Error fetching user details", t)
            onResult(emptyMap(), "Network error: ${t.message}")
        }
    })
}
