package com.example.gguessr.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gguessr.R
import com.example.gguessr.data.LoggedInPlayer


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMainMenu(navController: NavController) {
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Hintergrundbild
            Image(
                painter = painterResource(id = R.drawable.worldmap),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Vordergrund-Inhalt (Buttons etc.)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                //verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("GGuessr", style = MaterialTheme.typography.headlineMedium, color = textColor,
                    fontSize = 50.sp, fontWeight = FontWeight.ExtraBold,)
                Spacer(modifier = Modifier.fillMaxHeight(0.25f))
                // Obere Buttons: Die Spielvarianten
                FilledTonalButton(onClick = {
                    navController.navigate("standardgame")
                },  modifier = Modifier.padding(horizontal = 28.dp)) {
                    Text("Lokales Spiel" , color = textColor, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold )
                }

                FilledTonalButton(onClick = {
                    LoggedInPlayer.rankedGameStarted = true
                    navController.navigate("standardgame")
                },  modifier = Modifier.padding(horizontal = 100.dp)) {
                    Text("Ranked" , color = textColor, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold )
                }

                FilledTonalButton(onClick = {
                    LoggedInPlayer.timedGameStarted = true
                    navController.navigate("standardgame")
                },  modifier = Modifier.padding(horizontal = 30.dp)) {
                    Text("Zeitlimit" , color = textColor, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold )
                }

                Spacer(modifier = Modifier.fillMaxHeight(0.45f))
                // Untere Buttons: Zusätzliche Funktionen
                FilledTonalButton(onClick = {
                    navController.navigate("highscores")
                },  modifier = Modifier.padding(horizontal = 30.dp)) {
                    Text("Highscores" , color = textColor, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold )
                }
                FilledTonalButton(onClick = {
                    navController.navigate("proposelocation")
                },  modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Ort vorschlagen!" , color = textColor, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold )
                }
                FilledTonalButton(onClick = {
                    navController.navigate("tutorial")
                },  modifier = Modifier.padding(horizontal = 30.dp)) {
                    Text("Tutorial" , color = textColor, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold )
                }
            }
        }
    }
}

