package com.example.gguessr.screens

import android.util.Log
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

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}

@Composable
fun ScreenHighscores(){
    val vm: HighscoresVM = viewModel()
    val highscores by vm.highscores.collectAsState()
    val columnWeight = .33f // 30%
    val DATE_PATTERN = "MM/dd/yyyy"

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
            // Here is the header
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(Modifier.background(Color.Gray)) {
                    TableCell(text = "Name", weight = columnWeight)
                    TableCell(text = "Punkte", weight = columnWeight)
                    TableCell(text = "Datum", weight = columnWeight)
                }
            }
            // Here are all the lines of your table.
            items(highscores) { hs ->
                Row(Modifier.fillMaxWidth()) {
                    TableCell(text = hs.who, weight = columnWeight)
                    TableCell(text = hs.score.toString(), weight = columnWeight)
                    TableCell(text = hs.date, weight = columnWeight)
                }
            }
        }
    }
}
