package com.example.muselator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.muselator.ui.theme.primaryDark
import com.example.muselator.ui.theme.secondaryContainerLight
import com.example.muselator.ui.theme.surfaceLight

@Composable
fun MuselatorScreen(navController: NavController) {
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
                text = "Music is an enjoyable way to learn languages!" +
                        "\n\nChoose a song in a foreign language and have it translated for you!",
                color = surfaceLight,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 12.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            InputTextField("Pick song here:")

            CustomMintButton("Translate")

            Spacer(modifier = Modifier.height(30.dp))

            var outputText by remember { mutableStateOf<String?>(null) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = outputText.orEmpty(),
                    onValueChange = { outputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Translated Song") },
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircularPlusButton {  }
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