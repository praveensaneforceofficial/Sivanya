package com.example.sivanyaapp.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.sivanyaapp.R
import com.example.sivanyaapp.api.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf("Loading...") }
    var phone by remember { mutableStateOf("Loading...") }
    var address by remember { mutableStateOf("Loading...") }
    var loading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var profileImage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        fetchUserDetails(context) { user, error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            } else {
                fullName = user["full_name"] ?: "N/A"
                email = user["email"] ?: "N/A"
                phone = user["phone"] ?: "N/A"
                address = user["address"] ?: "N/A"
                profileImage = user["profile_image"] ?: ""  // Optional profile image
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

    fun saveProfile() {
        isSaving = true
        val updatedProfile = UserProfileUpdateRequest(fullName, email, phone, address)
        val apiService = RetrofitClient.instance.create(ApiInterface::class.java)
        val call = apiService.updateUserProfile(updatedProfile)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                isSaving = false
                if (response.isSuccessful) {
                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                isSaving = false
                Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { logoutUser() }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                if (profileImage.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(profileImage),
                        contentDescription = "Profile Image",
                        modifier = Modifier.size(100.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_user_placeholder),
                        contentDescription = "Placeholder Image",
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Editable Profile Fields
            ProfileInputField(label = "Full Name", value = fullName, onValueChange = { fullName = it })
            ProfileInputField(label = "Email", value = email, readOnly = true)
            ProfileInputField(label = "Phone", value = phone, onValueChange = { phone = it })
            ProfileInputField(label = "Address", value = address, onValueChange = { address = it }, maxLines = 4)

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = { saveProfile() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(text = if (isSaving) "Saving..." else "Save", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun SmallTopAppBar(title: @Composable () -> Unit, navigationIcon: @Composable () -> Unit, actions: @Composable () -> Unit) {

}

@Composable
fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    readOnly: Boolean = false,
    maxLines: Int = 1 // Default to a single-line input
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = { if (!readOnly) onValueChange(it) },
            readOnly = readOnly,
            modifier = Modifier.fillMaxWidth(),
            singleLine = maxLines == 1, // Prevents single-line restriction when maxLines > 1
            shape = RoundedCornerShape(12.dp),
            maxLines = maxLines // Allows multiline for text areas
        )
    }
}


fun fetchUserDetails(context: Context, onResult: (Map<String, String>, String?) -> Unit) {
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

    // Load cached data first
    val cachedUser = mapOf(
        "full_name" to (sharedPreferences.getString("full_name", "N/A") ?: "N/A"),
        "email" to (sharedPreferences.getString("email", "N/A") ?: "N/A"),
        "phone" to (sharedPreferences.getString("phone", "N/A") ?: "N/A"),
        "address" to (sharedPreferences.getString("address", "N/A") ?: "N/A"),
        "profile_image" to (sharedPreferences.getString("profile_image", "") ?: "")
    )
    onResult(cachedUser, null) // Show cached data immediately

    val userEmail = sharedPreferences.getString("email", null)
    if (userEmail == null) {
        onResult(emptyMap(), "User email not found. Please log in again.")
        return
    }

    // Fetch updated data from API
    val apiService = RetrofitClient.instance.create(ApiInterface::class.java)
    val call = apiService.getUserProfile(UserEmailRequest(userEmail))

    call.enqueue(object : Callback<UserProfileResponse> {
        override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    val newData = mapOf(
                        "full_name" to (user.full_name ?: "N/A"),
                        "email" to (user.email ?: "N/A"),
                        "phone" to (user.phone ?: "N/A"),
                        "address" to (user.address ?: "N/A"),
                        "profile_image" to ("")
                    )

                    // Store updated data in SharedPreferences
                    with(sharedPreferences.edit()) {
                        putString("full_name", newData["full_name"])
                        putString("email", newData["email"])
                        putString("phone", newData["phone"])
                        putString("address", newData["address"])
                        putString("profile_image", newData["profile_image"])
                        apply()
                    }

                    onResult(newData, null) // Update UI with fresh data
                } else {
                    onResult(emptyMap(), "Failed to parse user data.")
                }
            } else {
                onResult(emptyMap(), "Error: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
            onResult(emptyMap(), "Network error: ${t.message}")
        }
    })
}

