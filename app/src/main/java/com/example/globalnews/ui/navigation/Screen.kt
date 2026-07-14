package com.example.globalnews.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Category
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home_screen", "Головна", Icons.Default.Home)
    object Category : Screen("category_screen", "Категорії", Icons.Default.Category)
}