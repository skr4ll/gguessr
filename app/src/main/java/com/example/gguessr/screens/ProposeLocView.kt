package com.example.gguessr.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gguessr.R
import com.example.gguessr.viewmodels.ProposeLocVM

@Composable
fun ScreenProposeLoc(navController: NavController){
    val vm: ProposeLocVM = viewModel()
    var description = remember { mutableStateOf("") }
    var lat = remember { mutableStateOf("") }
    var lon = remember { mutableStateOf("") }
    var errText = remember { mutableStateOf("") }

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

    Text(
        modifier = Modifier.padding(16.dp, 32.dp),
        text = "Location vorschlagen",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        color = Color.Black,
        fontWeight = FontWeight.ExtraBold
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))

        TextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Beschreibung") },
            placeholder = { Text("Beschreibung") },
            singleLine = true,
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.01f))
        TextField(
            value = lat.value,
            onValueChange = { lat.value = it },
            //modifier = Modifier.padding(16.dp),
            label = { Text("Breitengrad") },
            singleLine = true,
            placeholder = { Text("Breitengrad") },
            )
        Spacer(modifier = Modifier.fillMaxHeight(0.01f))
        TextField(
            value = lon.value,
            onValueChange = { lon.value = it },
            //modifier = Modifier.padding(16.dp),
            label = { Text("Längengrad") },
            singleLine = true,
            placeholder = { Text("Längengrad") },
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.01f))
        if (errText.value.isNotEmpty()) {
            Text(
                text = errText.value,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }
        Button(onClick = {
            var d = description.value; var la = lat.value; var lo =  lon.value

            if (d.isNotEmpty() && la.isNotEmpty() && lo.isNotEmpty()) {
                vm.proposeLoc(d, la.toDouble(), lo.toDouble())
                description.value = ""
                lat.value = ""
                lon.value = ""
                errText.value = ""
            } else {
                errText.value = "Eingaben prüfen!"
            }
        }
        ) {
            Text("Vorschlagen")
        }
        Button(onClick = {navController.navigate("mainmenu")}) {
            Text("Zurück")
        }
        Text(
            text = "Die Location muss über StreetView verfügen. Jede Location wird geprüft und entweder in die Sammlung " +
                    "aufgenommen (eventuell mit Änderungen) oder abgelehnt.\n\n",
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black)
                .padding(8.dp) // Abstand zwischen Text und Rand
        )
    }
    }
}

