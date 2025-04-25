package com.example.muselator.api

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.*
import com.google.mlkit.common.model.DownloadConditions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A composable button that fetches lyrics for a given song and artist, detects the language of the lyrics,
 * and translates them to English. This function handles asynchronous operations including:
 * - Fetching lyrics
 * - Identifying the language of the lyrics
 * - Downloading the translation model if needed
 * - Translating the lyrics to English
 *
 * @param songTitle The title of the song to fetch lyrics for.
 * @param artistName The name of the artist performing the song.
 * @param isDetecting Boolean flag indicating whether detection is in progress.
 * @param coroutineScope The CoroutineScope used to launch background operations.
 * @param onStartDetecting Callback triggered when detection and translation start.
 * @param onFinishDetecting Callback triggered when detection and translation are finished.
 * @param onLyricsFetched Callback triggered with the fetched lyrics as a string.
 * @param onTranslationSuccess Callback triggered with the detected language code and translated text.
 * @param onTranslationFailure Callback triggered with an error message if translation fails.
 */
@Composable
fun FetchLyricsButton(
    songTitle: String,
    artistName: String,
    isDetecting: Boolean,
    coroutineScope: CoroutineScope,
    onStartDetecting: () -> Unit,
    onFinishDetecting: () -> Unit,
    onLyricsFetched: (String) -> Unit,
    onTranslationSuccess: (String, String) -> Unit,
    onTranslationFailure: (String) -> Unit
) {
    Button(
        onClick = {
            onStartDetecting()

            coroutineScope.launch {
                val lyricsResult = fetchLyrics(songTitle, artistName)
                onLyricsFetched(lyricsResult)

                if (lyricsResult.isNotEmpty()) {
                    val languageIdentifier = LanguageIdentification.getClient()

                    languageIdentifier.identifyLanguage(lyricsResult)
                        .addOnSuccessListener { languageCode ->
                            if (languageCode != "und") {
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
                                        englishTranslator.translate(lyricsResult)
                                            .addOnSuccessListener { translatedText ->
                                                onTranslationSuccess(languageCode, translatedText)
                                            }
                                            .addOnFailureListener {
                                                onTranslationFailure("Translation failed.")
                                            }
                                    }
                                    .addOnFailureListener {
                                        onTranslationFailure("Model download failed.")
                                    }

                            } else {
                                onTranslationFailure("Language not detected.")
                            }
                        }
                        .addOnFailureListener {
                            onTranslationFailure("Language detection failed.")
                        }
                        .addOnCompleteListener {
                            onFinishDetecting()
                        }
                } else {
                    onFinishDetecting()
                }
            }
        },
        enabled = songTitle.isNotEmpty() && !isDetecting,
        colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(125f, 0.32f, 0.64f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Fetch Lyrics and Translate")
    }
}