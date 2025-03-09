package com.example.muselator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muselator.ui.theme.MuselatorTheme
import com.example.muselator.ui.theme.onSecondaryContainerLight
import com.example.muselator.ui.theme.secondaryContainerLight
import com.example.muselator.ui.theme.tertiaryLight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuselatorApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MuselatorApp(){
    LandingScreen()
}

@Preview(showBackground = true)
@Composable
fun LandingScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Welcome!",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.surface
        )

        Button(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            onClick = {/* nothing */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = secondaryContainerLight
            )
            ) {
            Text(
                text = "Log In",
                color = tertiaryLight,
                fontSize = 25.sp
            )
        }

    }
}
