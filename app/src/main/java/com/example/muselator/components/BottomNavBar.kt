package com.example.muselator.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Represents an item in the bottom navigation bar.
 *
 * @param route The navigation route associated with the item.
 * @param label The text label displayed below the icon.
 * @param icon The icon to be displayed in the navigation bar.
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

/**
 * A composable bottom navigation bar that allows navigation between different screens.
 * Highlights the currently selected route and handles route changes using the NavController.
 *
 * @param navController The NavController used to manage navigation between screens.
 */
@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("homescreen", "Home", Icons.Default.Home),
        BottomNavItem("muselator", "Muselator", Icons.Default.Star),
        BottomNavItem("flashcards", "Flashcards", Icons.Default.Create),
        BottomNavItem("profile", "Profile", Icons.Default.Person)
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo("homescreen") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}