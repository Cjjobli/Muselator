package com.example.muselator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.muselator.ui.theme.surfaceLight
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

@Composable
fun HomeScreen(navController: NavController) {

    var inputText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }
    var detectedLanguage by remember { mutableStateOf("") }
    var isDetecting by remember { mutableStateOf(false) }

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
                text = "Welcome to Muselator!" +
                        "\nA music language learning tool" +
                        "\n\nTranslate anything here:",
                color = surfaceLight,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 12.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Text to translate", color = Color.White) },
                textStyle = TextStyle(fontSize = 30.sp, color = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = Color.White
                )
            )

            Button(
                onClick = {
                    isDetecting = true

                    // Detect language using ML Kit's Language Identification
                    val languageIdentifier = LanguageIdentification.getClient()

                    languageIdentifier.identifyLanguage(inputText)
                        .addOnSuccessListener { languageCode ->

                            if (languageCode != "und") {
                                detectedLanguage = "Detected language: $languageCode"

                                // Translate only if language is detected
                                val options = TranslatorOptions.Builder()
                                    .setSourceLanguage(languageCode)
                                    .setTargetLanguage(TranslateLanguage.ENGLISH)
                                    .build()

                                val englishTranslator = Translation.getClient(options)

                                val conditions = DownloadConditions.Builder()
                                    .requireWifi()
                                    .build()

                                englishTranslator.downloadModelIfNeeded(conditions)
                                    .addOnSuccessListener {
                                        englishTranslator.translate(inputText)
                                            .addOnSuccessListener {  translatedText = it }
                                            .addOnFailureListener { translatedText = "Translation failed." }
                                    }
                                    .addOnFailureListener {
                                        translatedText = "Model download failed."
                                    }

                                // Language is undetected
                            } else {
                                detectedLanguage = "Language not detected."
                            }
                        }
                        .addOnFailureListener {
                            detectedLanguage = "Language detection failed."
                        }
                        .addOnCompleteListener {
                            isDetecting = false
                        }
                },
                enabled = inputText.isNotEmpty() && !isDetecting,
                colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(125f, 0.32f, 0.64f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Detect and Translate")
            }

            Text(text = detectedLanguage, style = TextStyle(fontSize = 20.sp, color = Color.White), modifier = Modifier.padding(top = 12.dp))

            OutlinedTextField(
                value = translatedText,
                onValueChange = {},
                label = { Text("Translation", color = Color.White) },
                textStyle = TextStyle(fontSize = 30.sp, color = Color.White),
                shape = RoundedCornerShape(16.dp),
                readOnly = true,
                modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = Color.White
                    )
                )

            // Clear Button
            Button(
                onClick = {
                    inputText = ""
                    translatedText = ""
                    detectedLanguage = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(0f, 0.7f, 0.7f)),
                modifier = Modifier.fillMaxWidth().padding(top = 30.dp)
            ) {
                Text("Clear")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}


@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("homescreen", "Home", Icons.Default.Home),
        BottomNavItem("muselator", "Muselator", Icons.Default.Star),
        BottomNavItem("flashcards", "Flashcards", Icons.Default.Create),
        BottomNavItem("profile", "Profile", Icons.Default.Person)
    )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) { // Prevent crashing by avoiding duplicate navigation
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

data class BottomNavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
