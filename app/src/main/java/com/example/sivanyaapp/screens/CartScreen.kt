package com.example.sivanyaapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavHostController) {
    var totalPrice by remember { mutableStateOf(0) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Cart") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(text = "Your Cart is Empty!", fontSize = 20.sp, modifier = Modifier.padding(16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /* Proceed to Checkout */ }, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Checkout ($$totalPrice)")
            }
        }
    }
}

@Preview
@Composable
fun PreviewCartScreen() {
    CartScreen(navController = rememberNavController())
}
