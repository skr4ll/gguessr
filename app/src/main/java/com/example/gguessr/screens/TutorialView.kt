package com.example.gguessr.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
// Activity zur Nutzung der App
@Composable
@Preview
fun ScreenTutorial(){
    Text(
        modifier = Modifier.padding(16.dp, 28.dp),
        text = "Tutorial",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 80.dp),
        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        item {
            Text(text = "Spielvarianten:\n\n1. Lokales Spiel:\n\nDie erste Spieloption. Hier spielt man alleine " +
                    "und bekommt 5 zufällige Orte zugewiesen, die Anhand der StreetView " +
                    "erraten werden müssen. Es werden hierbei keine Highscores erfasst.\n\n")
        }
        item {
            Text(text = "2. Ranked: \n\nÄquivalent zum lokalen Standardspiel, außer dass die erzielten" +
                    " Highscores in Datenbank geschrieben werden und Online abrufbar sind.\n\n")
        }
        item {
            Text(text = "3. Zeitlimit: \n\n Hierbei hat man eine Minute Zeit, um 5 zufällige Locations so gut wie möglich" +
                    " zu erraten. Die erzielten Highscores werden in die Datenbank hochgeladen. In der Highscoreanzeige werden" +
                    " erzielte Highscores im Zeitlimitmodus in Gold dargestellt.\n\n")
        }
        item {
            Text(text = "Weitere Funktionen:\n\n" +
                    "1. Highscoreanzeige: \n\nHier werden alle erzielten Highscores in absteigender Reihenfolge angezeigt.\n\n")
        }
        item {
            Text(text = "2. Ort vorschlagen: \n\nHierbei kann der Nutzer selbst einen Ort vorschlagen. Dazu muss eine " +
                    "Beschreibung, sowie der Breiten- und Längengrad des Ortes in der Maske eingetragen werden.\n\n")
        }
        item {
            Text(text = "Weiteres: Die UX-Idee hinter der App, ist sehr simpel gehalten. Es werden Standardbuttons verwendet die gut sichtbar platziert sind. " +
                    "Im Spielmodus befinden sich die spielrelevanten Buttons wie \"Ort tippen\" oder \"Tipp bestätigen\" immer in der Bottom Bar der View. In der " +
                    "Top Bar werden die relevanten Informationen zu Rundenzahl und momentaner Punktzahl angezeigt.")
        }
    }

}



