package com.example.muselator

import androidx.compose.foundation.background
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
import androidx.navigation.compose.currentBackStackEntryAsState

/*
Try these songs out!
Tiroteo - Marc Segui
Per due come noi - Angelina mango
Beifahrer - Ayliva
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

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->

        //Scrollable
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Making the entire content scrollable
                    .padding(16.dp)
            ) {

                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    text = "Music is an enjoyable way to learn languages!" +
                            "\n\nChoose a song in a foreign language and have it translated for you!",
                    color = surfaceLight,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 12.dp)
                )

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

                Button(
                    onClick = {
                        isDetecting = true

                        // Fetch lyrics based on the song and artist names
                        coroutineScope.launch {
                            val lyricsResult = fetchLyrics(songTitle, artistName)
                            lyrics = lyricsResult

                            if (lyrics.isNotEmpty()) {
                                // Detect language and translate the lyrics
                                val languageIdentifier = LanguageIdentification.getClient()

                                languageIdentifier.identifyLanguage(lyrics)
                                    .addOnSuccessListener { languageCode ->

                                        if (languageCode != "und") {
                                            detectedLanguage = "Detected language: $languageCode"

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
                                                    englishTranslator.translate(lyrics)
                                                        .addOnSuccessListener { translatedText ->
                                                            translatedLyrics = translatedText
                                                        }
                                                        .addOnFailureListener {
                                                            translatedLyrics = "Translation failed."
                                                        }
                                                }
                                                .addOnFailureListener {
                                                    translatedLyrics = "Model download failed."
                                                }

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
                            }
                        }
                    },
                    enabled = songTitle.isNotEmpty() && !isDetecting,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(125f, 0.32f, 0.64f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Fetch Lyrics and Translate")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = detectedLanguage,
                    style = TextStyle(fontSize = 20.sp, color = Color.White),
                    modifier = Modifier.padding(top = 12.dp)
                )

                // Display original lyrics in a scrollable box
                Text(
                    text = "Lyrics:",
                    style = TextStyle(fontSize = 20.sp, color = Color.White),
                    modifier = Modifier.padding(top = 12.dp)
                )
                Box(modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp).padding(8.dp)) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(text = lyrics, style = TextStyle(fontSize = 18.sp, color = Color.White))
                    }
                }

                // Display translated lyrics in a scrollable box
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Translated Lyrics:",
                    style = TextStyle(fontSize = 20.sp, color = Color.White),
                    modifier = Modifier.padding(top = 12.dp)
                )
                Box(modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp).padding(8.dp)) {
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

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        songTitle = ""
                        artistName = ""
                        lyrics = ""
                        translatedLyrics = ""
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

/**
 * A suspend function to fetch lyrics for a given song and artist using the MusixMatch API.
 * Encodes song and artist names for safe URL usage, sends a request to the API,
 * and parses the JSON response to extract the lyrics.
 *
 * @param songTitle The title of the song.
 * @param artist The name of the artist.
 * @return The lyrics of the song if found, or a "Lyrics not found." message in case of an error.
 */
suspend fun fetchLyrics(songTitle: String, artist: String): String {
    return withContext(Dispatchers.IO) {
        try {
            // API Request
            val apiKey = "c7f526e6acc449fa1d91319fda70c4b5"
            val encodedTitle = URLEncoder.encode(songTitle, "UTF-8")
            val encodedArtist = URLEncoder.encode(artist, "UTF-8")
            val url = "https://api.musixmatch.com/ws/1.1/matcher.lyrics.get?q_track=$encodedTitle&q_artist=$encodedArtist&apikey=$apiKey"

            // Sends a GET request to MusixMatch.
            // Reads the entire response as a string.
            val response = URL(url).readText()

            // Parses the JSON structure layer by layer to get to lyrics_body
            val jsonObject = JSONObject(response)
            val lyrics = jsonObject.getJSONObject("message")
                .getJSONObject("body")
                .getJSONObject("lyrics")
                .getString("lyrics_body")

            lyrics
        } catch (e: Exception) {
            "Lyrics not found."
        }
    }
}

/**
 * A composable function to create a bottom navigation bar for the Muselator app.
 * Provides navigation between key screens such as Home, Muselator, Flashcards, and Profile,
 * while ensuring smooth transitions and preventing duplicate navigations.
 *
 * @param navController The NavController used for handling navigation between screens.
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}
