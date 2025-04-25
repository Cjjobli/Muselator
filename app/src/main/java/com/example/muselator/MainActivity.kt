package com.example.muselator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.muselator.Firebase.viewmodels.loginpages.SignupPage
import com.example.muselator.screens.ArtistScreen
import com.example.muselator.screens.FlashcardsScreen
import com.example.muselator.screens.HomeScreen
import com.example.muselator.screens.MuselatorScreen
import com.example.muselator.screens.ProfileScreen
import com.example.muselator.ui.theme.secondaryContainerLight
import com.example.muselator.ui.theme.surfaceLight
import com.example.muselator.ui.theme.tertiaryLight
import com.example.muselator.Firebase.viewmodels.AuthState
import com.example.muselator.Firebase.viewmodels.AuthViewModel
import com.example.muselator.components.CustomMintButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authViewModel : AuthViewModel by viewModels()
        setContent {
            MuselatorApp(authViewModel)
        }
    }
}

/**
 * A composable function that serves as the entry point for the Muselator app.
 * Sets up navigation between various screens using a NavHost.
 *
 * @Preview Shows a preview of the composable in the Android Studio design editor.
 */
@Composable
fun MuselatorApp(authViewModel: AuthViewModel){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "LandingScreen"){
        composable("landingscreen"){ LandingScreen(navController, authViewModel) }
        composable("homescreen"){ HomeScreen(navController) }
        composable("muselator") { MuselatorScreen(navController) }
        composable("flashcards") { FlashcardsScreen(navController) }
        composable("profile") { ProfileScreen(navController, authViewModel) }
        composable("ArtistScreen") { ArtistScreen(navController) }
        composable("signup") { SignupPage(navController, authViewModel)  }
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
fun LandingScreen(navController: NavController, authViewModel: AuthViewModel){

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    LaunchedEffect(authState.value){
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("homescreen")
            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(2.dp),
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
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.semantics { contentDescription = "Welcome!" }
        )

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = "Email", color = Color.White)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                cursorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = "Password", color = Color.White)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                cursorColor = Color.White
            )
        )

        TextButton(onClick = {
            navController.navigate("signup")
        }) {
            Text(text = "Don't have an account? Signup", color = Color.White)
        }

        CustomMintButton(
            text = "Login",
            onClick = {
                authViewModel.login(email, password)
            }
        )
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

