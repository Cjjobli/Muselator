package com.example.muselator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
fun MuselatorScreen() {
    val navController = rememberNavController()
    MuselatorScreen(navController)
}

@Composable
fun CircularPlusButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(60.dp)  // Set size to ensure it's large enough to be a circle
            .clip(CircleShape)  // Use CircleShape to make it circular
            .background(Color.White),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = "+",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}