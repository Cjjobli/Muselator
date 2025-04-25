package com.example.muselator.components

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muselator.ui.theme.secondaryContainerLight
import com.example.muselator.ui.theme.tertiaryLight
import java.util.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/**
 * Initializes and remembers a [TextToSpeech] instance tied to the current [Context].
 * The TTS instance is configured to use Canadian English and is properly cleaned up
 * when the composable leaves the composition.
 *
 * @return The initialized [TextToSpeech] instance or null if not yet available.
 */
@Composable
fun rememberTTS(): TextToSpeech? {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.CANADA
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    return tts
}


/**
 * A composable button that uses a [TextToSpeech] instance to speak the provided text aloud.
 * When clicked, it triggers the provided `onStartSpeaking` callback, speaks the text,
 * and uses the button label for accessibility.
 *
 * @param tts The [TextToSpeech] instance used to speak the text.
 * @param text The content to be spoken aloud.
 * @param label The button label and content description for accessibility.
 * @param onStartSpeaking Lambda invoked when speaking starts.
 * @param onStopSpeaking Lambda invoked when speaking should be stopped (not used here, but passed for future control).
 */
@Composable
fun SpeakButton(
    tts: TextToSpeech?,
    text: String,
    label: String,
    onStartSpeaking: () -> Unit, // Lambda function that signals when to start speaking
    onStopSpeaking: () -> Unit // Lambda function for stopping speech
) {
    ElevatedButton(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .semantics { contentDescription = label },
        onClick = {
            onStartSpeaking() // Trigger onStartSpeaking before speaking
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        },
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = secondaryContainerLight)
    ) {
        Text(
            text = label,
            color = tertiaryLight,
            fontSize = 25.sp
        )
    }
}

