package com.example.muselator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.muselator.ui.theme.surfaceLight

@Composable
fun FlashcardsScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Create a deck of flashcards here",
                color = surfaceLight,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(start = 12.dp)
            )

            CustomMintButton("Create Set")

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Your sets:",
                color = surfaceLight,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 12.dp)
            )

            // Card Carousel using LazyRow
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                items(5) { index ->
                    Card(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .width(150.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .height(200.dp)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Flashcard Set #$index", // Replace with actual set name
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(35.dp))

                            Button(
                                onClick = {
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("Practice")
                            }

                        }
                    }
                }
            }

            CustomMintButton("Practice All")

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Total cards practiced: x",
                color = surfaceLight,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)
            )

            Text(
                text = "Last session review: 2024-03-09",
                color = surfaceLight,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 12.dp)
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlashcardsScreen() {
    val navController = rememberNavController()
    FlashcardsScreen(navController)
}
