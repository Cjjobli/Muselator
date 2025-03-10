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