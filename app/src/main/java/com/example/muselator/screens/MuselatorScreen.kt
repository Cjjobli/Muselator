package com.example.muselator.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URLEncoder
import java.net.URL
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.muselator.api.FetchLyricsButton
import com.example.muselator.components.BottomNavBar
import com.example.muselator.components.SpeakButton
import com.example.muselator.components.StopButton
import com.example.muselator.components.rememberTTS
import com.example.muselator.ui.theme.secondaryContainerLight
import java.util.Locale

/*
Try these songs out!
Tiroteo - Marc Segui
Per due come noi - Angelina mango
Beifahrer - Ayliva
Mo-do eins, zwei, polizei
*/

/**
 * A composable function that defines the Muselator screen.
 * This screen enables users to input a song title and artist name, fetch lyrics,
 * identify the language, and translate the lyrics into English.
 * It also provides options to view the original and translated lyrics and clear all fields.
 *
 * @param navController The NavController used for handling navigation between screens.
 */
@Composable
fun MuselatorScreen(navController: NavController) {

    var songTitle by remember { mutableStateOf("") }
    var artistName by remember { mutableStateOf("") }
    var lyrics by remember { mutableStateOf("") }
    var translatedLyrics by remember { mutableStateOf("") }
    var detectedLanguage by remember { mutableStateOf("") }
    var isDetecting by remember { mutableStateOf(false) }

    var lyricsFetched by remember { mutableStateOf(false) }
    var translationFetched by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val tts = rememberTTS()

    var isSpeaking by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Music is an enjoyable way to learn languages!" +
                            "\n\nChoose a song in a foreign language and have it translated for you!",
                    color = surfaceLight,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        navController.navigate("ArtistScreen")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Artist Screen", color = Color.White)
                }

                Spacer(modifier = Modifier.height(50.dp))

                OutlinedTextField(
                    value = songTitle,
                    onValueChange = { songTitle = it },
                    label = { Text("Enter Song Title", color = Color.White) },
                    textStyle = TextStyle(fontSize = 30.sp, color = Color.White),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.White,
                        focusedBorderColor = Color.White
                    )
                )

                OutlinedTextField(
                    value = artistName,
                    onValueChange = { artistName = it },
                    label = { Text("Enter Artist Name", color = Color.White) },
                    textStyle = TextStyle(fontSize = 30.sp, color = Color.White),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.White,
                        focusedBorderColor = Color.White
                    )
                )

                FetchLyricsButton(
                    songTitle = songTitle,
                    artistName = artistName,
                    isDetecting = isDetecting,
                    coroutineScope = coroutineScope,
                    onStartDetecting = { isDetecting = true },
                    onFinishDetecting = { isDetecting = false },
                    onLyricsFetched = { lyrics = it; lyricsFetched = true },
                    onTranslationSuccess = { languageCode, translatedText ->
                        detectedLanguage = "Detected language: $languageCode"
                        translatedLyrics = translatedText
                        translationFetched = true
                    },
                    onTranslationFailure = { errorMessage ->
                        detectedLanguage = errorMessage
                        translatedLyrics = ""
                        translationFetched = false
                    }
                )

                if (lyricsFetched) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Lyrics:",
                        style = TextStyle(fontSize = 20.sp, color = Color.White),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .border(2.dp, secondaryContainerLight)
                        .padding(8.dp)
                    ) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            Text(text = lyrics, style = TextStyle(fontSize = 18.sp, color = Color.White))
                        }
                    }
                }

                if (translationFetched) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Translated Lyrics:",
                        style = TextStyle(fontSize = 20.sp, color = Color.White),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .border(2.dp, secondaryContainerLight)
                        .padding(8.dp)
                    ) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            val formattedLyrics = formatTranslatedText(translatedLyrics)
                            formattedLyrics.forEach { line ->
                                Text(
                                    text = line,
                                    style = TextStyle(fontSize = 18.sp, color = Color.White),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }
                    }
                }

                if (lyricsFetched) {
                    SpeakButton(
                        text = lyrics,
                        tts = tts,
                        label = "Speak Detected Language",
                        onStartSpeaking = { isSpeaking = true },
                        onStopSpeaking = {
                            tts?.stop()
                            isSpeaking = false
                        }
                    )
                }

                if (translationFetched) {
                    SpeakButton(
                        text = translatedLyrics,
                        tts = tts,
                        label = "Speak Translated Lyrics",
                        onStartSpeaking = { isSpeaking = true },
                        onStopSpeaking = {
                            tts?.stop()
                            isSpeaking = false
                        }
                    )
                }

                if (isSpeaking) {
                    StopButton(
                        onStop = {
                            tts?.stop()
                            isSpeaking = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        songTitle = ""
                        artistName = ""
                        lyrics = ""
                        translatedLyrics = ""
                        detectedLanguage = ""
                        lyricsFetched = false
                        translationFetched = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(0f, 0.7f, 0.7f)),
                    modifier = Modifier.fillMaxWidth().padding(top = 30.dp)
                ) {
                    Text("Clear")
                }
            }
        }
    }
}

/**
 * A function to format the translated text into a list of lines.
 * Splits the text based on punctuation marks (e.g., '.', ',', '!', '?') followed by whitespace.
 *
 * @param text The input text to be formatted.
 * @return A list of strings, each representing a line of the formatted text.
 */
fun formatTranslatedText(text: String): List<String> {
    val lines = text.split(Regex("(?<=\\.|,|!|\\?)\\s+"))
    return lines
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}
