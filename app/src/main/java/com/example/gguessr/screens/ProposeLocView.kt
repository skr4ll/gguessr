package com.example.gguessr.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gguessr.viewmodels.ProposeLocVM

@Composable
fun ScreenProposeLoc(){
    val vm: ProposeLocVM = viewModel()
    var description = remember { mutableStateOf("") }
    var lat = remember { mutableStateOf("") }
    var lon = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Beschreibung") },
            placeholder = { Text("Beschreibung") },
            singleLine = true,
        )
        OutlinedTextField(
            value = lat.value,
            onValueChange = { lat.value = it },
            //modifier = Modifier.padding(16.dp),
            label = { Text("Breitengrad") },
            singleLine = true,
            placeholder = { Text("Breitengrad") },
            )
        OutlinedTextField(
            value = lon.value,
            onValueChange = { lon.value = it },
            //modifier = Modifier.padding(16.dp),
            label = { Text("Längengrad") },
            singleLine = true,
            placeholder = { Text("Längengrad") },
        )
        Button(onClick = {
            if(description.value.isNotEmpty() && lat.value.isNotEmpty() && lon.value.isNotEmpty()) {
                vm.proposeLoc(description.toString(), lat.value.toDouble(), lon.value.toDouble())
            }
        }
        ) {
            Text("Vorschlagen")
        }
    }
}

