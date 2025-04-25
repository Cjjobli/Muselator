package com.example.muselator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muselator.ui.theme.secondaryContainerLight
import com.example.muselator.ui.theme.tertiaryLight


/**
 * A composable function to create a custom-styled button.
 * Supports an optional click action and displays the provided text.
 *
 * @param text The label text displayed on the button.
 * @param onClick Optional lambda function to handle button click actions. Defaults to an empty action if not provided.
 */
@Composable
fun CustomMintButton(text: String, onClick: (() -> Unit)? = null) {
    ElevatedButton(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .semantics { contentDescription = text },
        onClick = onClick ?: {},
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = secondaryContainerLight)
    ) {
        Text(
            text = text,
            color = tertiaryLight,
            fontSize = 25.sp
        )
    }
}

/**
 * A composable function that creates a button labeled "Stop Speaking".
 * Executes the provided callback when clicked.
 *
 * @param onStop Lambda function invoked when the button is clicked.
 */
@Composable
fun StopButton(onStop: () -> Unit) {
    Button(onClick = onStop) {
        Text("Stop Speaking")
    }
}

/**
 * A composable function that displays a circular '+' button.
 * Commonly used for actions like adding a new item.
 *
 * Not used yet - Need for flashcard section
 * @param onClick Lambda function invoked when the button is clicked.
 */
@Composable
fun CircularPlusButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
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
