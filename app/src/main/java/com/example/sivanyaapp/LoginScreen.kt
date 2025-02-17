package com.example.sivanyaapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.OffsetMapping
import androidx.navigation.compose.rememberNavController

// Custom Password Visual Transformation (since Material3 doesn't have it)
class PasswordVisualTransformation : VisualTransformation {
    override fun filter(text: androidx.compose.ui.text.AnnotatedString): TransformedText {
        val transformedText = "•".repeat(text.length) // Mask password with bullets (•)
        return TransformedText(androidx.compose.ui.text.AnnotatedString(transformedText), OffsetMapping.Identity)
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    // State variables for username, password, and error message
    val username = remember { mutableStateOf<String>("") }
    val password = remember { mutableStateOf<String>("") }
    val errorMessage = remember { mutableStateOf<String>("") }

    // Handle the login click event
    fun onLoginClick() {
        // Check for the credentials (You can add more complex validation later)
        if (username.value == "test" && password.value == "test") {
            // If credentials are correct, navigate to MainActivity (Home)
            navController.navigate("home")
        } else {
            // If credentials are incorrect, show an error message
            errorMessage.value = "Invalid username or password."
        }
    }

    // Login Page UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Login",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Username Field
        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field with custom transformation for hiding password
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(), // Use custom transformation
            singleLine = true
        )

        // Error Message if any
        if (errorMessage.value.isNotEmpty()) {
            Text(
                text = errorMessage.value,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium, // Use bodyMedium for Material3
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = { onLoginClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // Provide NavController to the LoginScreen Preview
    LoginScreen(navController = rememberNavController())
}
