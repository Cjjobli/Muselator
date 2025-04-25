package com.example.muselator.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder

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
            val apiKey = "" // put your api key here

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