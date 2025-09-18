package com.example.gguessr.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gguessr.data.Highscore
import com.example.gguessr.viewmodels.HighscoresVM
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.gguessr.R

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    textColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.DarkGray
) {
    Text(
        text = text,
        color = textColor,
        modifier = Modifier
            .weight(weight)
            .background(backgroundColor)
            .border(1.dp, Color.Black)
            .padding(8.dp)
    )
}

@Composable
fun ScreenHighscores(navController: NavController){
    val vm: HighscoresVM = viewModel()
    val highscores by vm.highscores.collectAsState()
    val columnWeight = .25f // 25%
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
    if (highscores.isEmpty()){
        // Daten sind noch nicht geladen. Wir warten auf die Antwort der DB
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Lade Highscores...")
        }
    }
    else {
        LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(Modifier.background(Color.Gray)) {
                    TableCell(text = "Mode", weight = columnWeight)
                    TableCell(text = "Name", weight = columnWeight)
                    TableCell(text = "Punkte", weight = columnWeight)
                    TableCell(text = "Datum", weight = columnWeight)
                }
            }
            items(highscores) { hs ->
                Row(Modifier.fillMaxWidth()) {
                    TableCell(text = hs.mode, weight = columnWeight, textColor = if (hs.mode == "timed") Color(0xFFFFD700) else Color.White)
                    TableCell(text = hs.who, weight = columnWeight, textColor = if (hs.mode == "timed") Color(0xFFFFD700) else Color.White)
                    TableCell(text = hs.score.toString(), weight = columnWeight, textColor = if (hs.mode == "timed") Color(0xFFFFD700) else Color.White)
                    TableCell(text = hs.date, weight = columnWeight, textColor = if (hs.mode == "timed") Color(0xFFFFD700) else Color.White)
                }
            }
            item{
                Button(onClick = {navController.navigate("mainmenu")}) {
                    Text("Zurück")
                }
            }
        }
        }
    }
}

