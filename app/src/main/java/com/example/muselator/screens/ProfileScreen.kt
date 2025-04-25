package com.example.muselator.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.muselator.components.CustomMintButton
import com.example.muselator.components.BottomNavBar
import com.example.muselator.ui.theme.surfaceLight
import com.example.muselator.Firebase.viewmodels.AuthState
import com.example.muselator.Firebase.viewmodels.AuthViewModel

/**
 * Composable function that displays the user's profile screen.
 * Shows greeting, membership date, and options like exporting cards,
 * deleting the account, and logging out.
 *
 * @param navController Navigation controller used to navigate between screens.
 * @param authViewModel ViewModel handling authentication logic and state.
 */
@Composable
fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel) {

    val authState = authViewModel.authState.observeAsState()

    // Automatically navigate to landing/login screen when logged out
    LaunchedEffect(authState.value) {
        if (authState.value == AuthState.Unauthenticated) {
            navController.navigate("landingscreen") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hello Cj!",
                color = surfaceLight,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(top = 50.dp)
            )

            Text(
                text = "You have been a member since 2025 March 9!",
                color = surfaceLight,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 20.dp, bottom = 50.dp)
            )

            CustomMintButton("Export Cards")

            CustomMintButton("Delete Account")

            CustomMintButton(
                text = "Log Out",
                onClick = {
                    authViewModel.signout()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()

    val dummyViewModel = AuthViewModel()

    ProfileScreen(navController = navController, authViewModel = dummyViewModel)
}
