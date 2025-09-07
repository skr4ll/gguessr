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
import com.example.gguessr.viewmodels.LoginVM
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ScreenLogin(navController: NavController ) {
    val vm: LoginVM= viewModel()
    var password = remember { mutableStateOf("") }
    var player = remember { mutableStateOf("") }
    var loginTap = remember { mutableStateOf(false) }
    val loginSuccess by vm.loginSuccess.collectAsState()
//    val playerName by vm.playerName.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,


    ){
        OutlinedTextField(
            value = player.value,
            onValueChange = { player.value = it },
            label = { Text("Name") },
            placeholder = { Text("Player") },
            singleLine = true,
        )

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier.padding(16.dp),
            label = { Text("Passwort") },
            singleLine = true,
            placeholder = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)

        )

        Button(onClick = {
            vm.attemptLogin(player.value, password.value)
            loginTap.value = true
        }) {
            Text("Login")
        }
        LaunchedEffect(loginTap.value) {
            if (!loginSuccess && loginTap.value) {
                Toast.makeText(context, "Falsche Daten!", Toast.LENGTH_LONG).show()
                loginTap.value = false
            }
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

