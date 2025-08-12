package com.example.gguessr.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMainMenu(navController: NavController)
{
    // Scaffold bietet eine Grundstruktur für die Auteilung eines Screens
    Scaffold(
        topBar =
            {
                TopAppBar(title = { Text("GGuessr") })
            },
        bottomBar =
            {
                BottomAppBar { Text("FUCK THIS SHIT!!111111elf") }
            },
    )

    {
        innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )

        {
            Button(onClick = { println("IwAs") })
            {
                Text("test")
            }
            Button(onClick = { navController.navigate("standardgame")})
            {
                Text("Standardspiel")
            }
        }
    }
}

