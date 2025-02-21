package com.example.sivanyaapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun CustomBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent), // Keeps it visually distinct
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp) // Slight shadow for depth
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF6200EE), Color(0xFFBB86FC))
                    )
                )
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomNavItem("home", Icons.Filled.Home, currentRoute, navController)
            CustomNavItem("categories", Icons.Filled.Favorite, currentRoute, navController)
            CustomNavItem("cart", Icons.Filled.ShoppingCart, currentRoute, navController)
            CustomNavItem("profile", Icons.Filled.Person, currentRoute, navController)
        }
    }
}

@Composable
fun CustomNavItem(route: String, icon: androidx.compose.ui.graphics.vector.ImageVector, currentRoute: String?, navController: NavHostController) {
    val isSelected = currentRoute == route
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { navController.navigate(route) }
            .padding(4.dp) // Adjust spacing
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color.White else Color.LightGray,
            modifier = Modifier.size(24.dp) // Smaller icon
        )
        if (isSelected) {
            Spacer(modifier = Modifier.height(3.dp))
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(3.dp)
                    .background(Color.White)
            )
        }
    }
}
