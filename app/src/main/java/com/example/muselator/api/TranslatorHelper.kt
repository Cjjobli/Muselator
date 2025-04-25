package com.example.muselator.api

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

/**
 * Detects the language of the given input text and translates it to English.
 * Utilizes ML Kit's language identification and translation APIs.
 *
 * @param inputText The text to be analyzed and translated.
 * @param onDetected Callback invoked with the detected language code (e.g., "Detected language: es").
 * @param onTranslated Callback invoked with the translated English text.
 * @param onError Callback invoked with an error message if detection or translation fails.
 * @param onComplete Callback invoked when all processing (success or failure) is complete.
 */
fun detectAndTranslate(
    inputText: String,
    onDetected: (String) -> Unit,
    onTranslated: (String) -> Unit,
    onError: (String) -> Unit,
    onComplete: () -> Unit
) {
    val languageIdentifier = LanguageIdentification.getClient()

    languageIdentifier.identifyLanguage(inputText)
        .addOnSuccessListener { languageCode ->
            if (languageCode != "und") {
                onDetected("Detected language: $languageCode")

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
                            .addOnSuccessListener { onTranslated(it) }
                            .addOnFailureListener { onError("Translation failed.") }
                    }
                    .addOnFailureListener {
                        onError("Model download failed.")
                    }

            } else {
                onError("Language not detected.")
            }
        }
        .addOnFailureListener {
            onError("Language detection failed.")
        }
        .addOnCompleteListener {
            onComplete()
        }
}