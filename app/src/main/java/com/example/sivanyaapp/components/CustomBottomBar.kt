package com.example.sivanyaapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
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
            .padding(bottom = 20.dp) // Small bottom padding
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .shadow(10.dp, RoundedCornerShape(50)) // Floating effect
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFFFF0266), Color(0xFFFF6699))
                    ),
                    shape = RoundedCornerShape(50)
                )
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround
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
    Box(
        modifier = Modifier
            .size(50.dp)
            .clickable { navController.navigate(route) }
            .background(
                color = if (currentRoute == route) Color.White else Color.Transparent,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (currentRoute == route) Color(0xFFFF0266) else Color.White, // Highlight active tab
            modifier = Modifier.size(28.dp)
        )
    }
}
