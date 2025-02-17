package com.example.sivanyaapp.screens

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

@Composable
fun ForgetPasswordScreen(navController: NavHostController) {
    var emailOrPhone by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }

    fun sendOTP() {
        otpSent = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Forgot Password",
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        TextField(
            value = emailOrPhone,
            onValueChange = { emailOrPhone = it },
            label = { Text("Enter Email or Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { sendOTP() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send OTP")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (otpSent) {
            Text(
                text = "OTP Sent Successfully!",
                color = androidx.compose.ui.graphics.Color.Green
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewForgetPasswordScreen() {
    ForgetPasswordScreen(navController = rememberNavController())
}
