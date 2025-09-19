package com.example.gguessr.screens

import com.example.gguessr.viewmodels.RegisterVM
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gguessr.R
import com.example.gguessr.data.LoggedInPlayer


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ScreenRegister(navController: NavController ) {
    val vm: RegisterVM = viewModel()
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
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = player.value,
                onValueChange = { player.value = it },
                label = { Text("Name") },
                placeholder = { Text("Name") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Passwort") },
                singleLine = true,
                placeholder = { Text("Passwort") },

                )
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
            Button(onClick = {

                if (player.value.isNotEmpty() && password.value.isNotEmpty()) {
                    vm.attemptRegister(player.value, password.value) { success ->
                        if (success) {
                            Toast.makeText(
                                context,
                                "${LoggedInPlayer.playerName} wurde registriert",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate("mainmenu")
                        } else {
                            Toast.makeText(
                                context,
                                "Spielername existiert bereits!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                else{
                    Toast.makeText(
                        context,
                        "Name/PW leer!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            {
                Text("Registrieren")
            }
        }
    }
}

