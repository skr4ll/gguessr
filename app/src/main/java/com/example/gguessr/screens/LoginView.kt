package com.example.gguessr.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gguessr.R
import com.example.gguessr.data.LoggedInPlayer
import com.example.gguessr.viewmodels.LoginVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLogin(navController: NavController ) {
    val vm: LoginVM = viewModel()
    var password = remember { mutableStateOf("") }
    var player = remember { mutableStateOf("") }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Hintergrundbild
        Image(
            painter = painterResource(id = R.drawable.worldmap),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Text("GGuessr", style = MaterialTheme.typography.headlineMedium, color = Color.Black,
                fontSize = 50.sp, fontWeight = FontWeight.ExtraBold,)
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            TextField(
                value = player.value,
                onValueChange = { player.value = it },
                label = { Text("Name") },
                placeholder = { Text("Name") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                //modifier = Modifier.padding(16.dp),
                label = { Text("Passwort") },
                singleLine = true,
                placeholder = { Text("Passwort") },

                )
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
            ElevatedButton(onClick = {
                vm.attemptLogin(player.value, password.value) { success ->
                    if (success) {
                        Toast.makeText(
                            context,
                            "Hi ${LoggedInPlayer.playerName}",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate("mainmenu")
                    } else {
                        Toast.makeText(context, "Falsche Daten!", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text("Login")
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Button(onClick = { navController.navigate("register") })
            {
                Text("Neuer Spieler")
            }
        }
    }
}

