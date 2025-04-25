package com.example.muselator.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

/**
 * Displays an Artist Search Screen for finding artist details.
 * Includes search input, artist info, albums, and error handling.
 *
 * @param navController Used for navigating between screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(navController: NavController) {
    var artistName by remember { mutableStateOf("") }
    var artistCountry by remember { mutableStateOf("") }
    var artistRating by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var artistId by remember { mutableStateOf("") }
    var albums by remember { mutableStateOf<List<String>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Function to search for the artist based on the name input
    fun searchArtist() {
        if (artistName.isNotBlank()) {
            coroutineScope.launch {
                val result = fetchArtistDataByName(artistName)
                result?.let {
                    artistId = it.id
                    artistCountry = it.country
                    artistRating = it.rating.toString()
                    errorMessage = ""
                    // Fetch the albums after finding the artist
                    fetchArtistAlbums(artistId) { fetchedAlbums ->
                        albums = fetchedAlbums
                    }
                } ?: run {
                    errorMessage = "Artist not found."
                }
            }
        } else {
            errorMessage = "Please enter an artist name."
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artist Search") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Gray)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TextField(
                value = artistName,
                onValueChange = { artistName = it },
                label = { Text("Artist Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { searchArtist() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }
            Spacer(Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 20.sp)
            } else {
                if (artistId.isNotEmpty()) {
                    Text("Artist Name: $artistName", fontSize = 24.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Country: $artistCountry", fontSize = 20.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Rating: $artistRating", fontSize = 20.sp)

                    // Display albums
                    if (albums.isNotEmpty()) {
                        Spacer(Modifier.height(16.dp))
                        Text("Albums:", fontSize = 24.sp)
                        Spacer(Modifier.height(8.dp))
                        albums.forEach { album ->
                            Text(album, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * A suspend function to fetch artist data based on the given name.
 * Sends a request to the MusixMatch API, parses the response, and extracts
 * details such as artist ID, name, country, and rating.
 *
 * @param artistName The name of the artist to search for.
 * @return An ArtistInfo object containing the artist's details, or null if the artist is not found or an error occurs.
 */
suspend fun fetchArtistDataByName(artistName: String): ArtistInfo? {
    return withContext(Dispatchers.IO) {
        try {
            val apiKey = "c7f526e6acc449fa1d91319fda70c4b5"
            val url = "https://api.musixmatch.com/ws/1.1/artist.search?q_artist=$artistName&apikey=$apiKey"
            val response = URL(url).readText()
            val json = JSONObject(response)
            val artist = json.getJSONObject("message")
                .getJSONObject("body")
                .getJSONArray("artist_list")
                .getJSONObject(0)

            ArtistInfo(
                id = artist.getJSONObject("artist").getString("artist_id"),
                name = artist.getJSONObject("artist").getString("artist_name"),
                country = artist.getJSONObject("artist").getString("artist_country"),
                rating = artist.getJSONObject("artist").getInt("artist_rating")
            )
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * A suspend function to fetch a list of albums for a given artist using their ID.
 * Sends a request to the MusixMatch API, retrieves the response, and parses the album names.
 * Updates the UI by invoking the provided callback with the list of album names.
 *
 * @param artistId The unique ID of the artist whose albums are to be fetched.
 * @param setAlbums A callback function to update the UI with the list of album names.
 */
suspend fun fetchArtistAlbums(artistId: String, setAlbums: (List<String>) -> Unit) {
    withContext(Dispatchers.IO) {
        try {
            val apiKey = "c7f526e6acc449fa1d91319fda70c4b5"
            val url = "https://api.musixmatch.com/ws/1.1/artist.albums.get?artist_id=$artistId&apikey=$apiKey"
            val response = URL(url).readText()
            val json = JSONObject(response)
            val albumsArray = json.getJSONObject("message")
                .getJSONObject("body")
                .getJSONArray("album_list")

            val albumNames = mutableListOf<String>()

            for (i in 0 until albumsArray.length()) {
                val album = albumsArray.getJSONObject(i).getJSONObject("album")
                albumNames.add(album.getString("album_name"))
            }

            // Update the UI with album names
            setAlbums(albumNames)
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * A data class representing information about an artist.
 * Stores essential details such as the artist's ID, name, country, and rating.
 *
 * @param id The unique identifier for the artist.
 * @param name The name of the artist.
 * @param country The country associated with the artist.
 * @param rating The artist's rating, typically represented as an integer.
 */
data class ArtistInfo(
    val id: String,
    val name: String,
    val country: String,
    val rating: Int
)
