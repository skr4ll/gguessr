package com.example.gguessr.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gguessr.data.LoggedInPlayer
import com.example.gguessr.viewmodels.LoginVM
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ScreenLogin(navController: NavController ) {
    val vm: LoginVM = viewModel()
    var password = remember { mutableStateOf("") }
    var player = remember { mutableStateOf("") }
    val loginSuccess by vm.loginSuccess.collectAsState()
    val loginMessage by vm.loginMessage.collectAsState()

//    val playerName by vm.playerName.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        TextField(
            value = player.value,
            onValueChange = { player.value = it },
            label = { Text("Name") },
            placeholder = { Text("Name") },
            singleLine = true,
        )

        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            //modifier = Modifier.padding(16.dp),
            label = { Text("Passwort") },
            singleLine = true,
            placeholder = { Text("Passwort") },

        )

        Button(onClick = {
            vm.attemptLogin(player.value, password.value) { success ->
                if (success) {
                    Toast.makeText(context, "Hi ${LoggedInPlayer.playerName}", Toast.LENGTH_SHORT).show()
                    navController.navigate("mainmenu")
                } else {
                    Toast.makeText(context, "Falsche Daten!", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Login")
        }

        // ToDo Auf Registrierungsseite verweisen
        Button(onClick = { })
        {
            Text("Neuer Spieler")
        }
    }
    if (loginSuccess) {
        navController.navigate("mainmenu")
    }
}

