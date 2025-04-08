package com.example.muselator

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.muselator.ui.theme.MuselatorTheme
import com.example.muselator.ui.theme.onSecondaryContainerLight
import com.example.muselator.ui.theme.secondaryContainerLight
import com.example.muselator.ui.theme.surfaceLight
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

/**
 * A composable function that serves as the entry point for the Muselator app.
 * Sets up navigation between various screens using a NavHost.
 *
 * @Preview Shows a preview of the composable in the Android Studio design editor.
 */
@Preview(showBackground = true)
@Composable
fun MuselatorApp(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "LandingScreen"){
        composable("landingscreen"){ LandingScreen(navController) }
        composable("homescreen"){ HomeScreen(navController) }
        composable("muselator") { MuselatorScreen(navController) }
        composable("flashcards") { FlashcardsScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
    }
}

/**
 * A composable function to render the Landing Screen of the app.
 * Includes a welcome message, input fields for user credentials,
 * and navigation options for login or account creation.
 *
 * @param navController The NavController used for handling navigation between screens.
 */
@Composable
fun LandingScreen(navController: NavController){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.appicon),
            contentDescription = "App Logo",
            modifier = Modifier
                .padding(top = 50.dp, bottom = 30.dp)
                .size(250.dp)
        )
        Text(
            text = "Welcome!",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.surface
        )

        InputTextField("Username")
        InputTextField("Password")

        CustomMintButton("Login"){
            navController.navigate("homescreen")
        }

        Text(
            text = "Forgot password?",
            color = surfaceLight,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Don't have an account?",
            color = surfaceLight,
            fontSize = 18.sp
        )

        CustomMintButton("Create"){
            navController.navigate("homescreen")
        }

    }
}

/**
 * A composable function to render a text input field.
 * Allows users to input text, with dynamic state management
 * and a customizable label.
 *
 * @param stringTxt Label text displayed above the input field.
 */
@Composable
fun InputTextField(stringTxt: String) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { newText -> text = newText },
        label = { Text( stringTxt ) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

    )
}

/**
 * A composable function to render a read-only text field.
 * Displays provided output text with a customizable label.
 *
 * @param stringTxt Label text displayed above the output field.
 * @param outputText Text to be displayed inside the read-only field. If null, it defaults to an empty string.
 */
@Composable
fun OutputTextField(stringTxt: String, outputText: String?) {
    TextField(
        value = outputText?: "",
        onValueChange = {},
        label = { Text(stringTxt) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        readOnly = true
    )
}

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
            .fillMaxWidth(),
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