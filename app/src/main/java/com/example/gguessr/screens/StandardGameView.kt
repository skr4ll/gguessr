package com.example.gguessr.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gguessr.data.LoggedInPlayer
import com.example.gguessr.viewmodels.GamePhase
import com.example.gguessr.viewmodels.StandardGameVM
import com.example.gguessr.viewmodels.totalRounds
import com.google.android.gms.maps.StreetViewPanoramaOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.streetview.StreetView
import com.google.maps.android.ktx.MapsExperimentalFeature

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(MapsExperimentalFeature::class, ExperimentalMaterial3Api::class)
@MapsExperimentalFeature
@Composable
fun ScreenStandardGame(navController: NavController) {
    val vm: StandardGameVM = viewModel()
    val phase by vm.phase.collectAsState()
    val round by vm.round.collectAsState()
    val score by vm.score.collectAsState()
    val currentLocation by vm.currentLocation.collectAsState()
    val guessPosition by vm.guessPosition.collectAsState()

    // Siehe else Block. Hier steigen wir nur ein wenn es bereits eine  currentLocation gibt.
    if (currentLocation != null) {
        Scaffold(
            topBar = {
                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    TopAppBar(title = { Text("Runde $round/$totalRounds --- Score: $score") })
                }

            },
            bottomBar = {
                when (phase) {
                    GamePhase.StreetView -> {
                        Button(onClick = { vm.startGuessing() }) {
                            Text("Ort tippen")
                        }
                    }

                    GamePhase.Guessing -> {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Button(
                                onClick = { vm.returnToStreetView() },
                                modifier = Modifier.padding(1.dp)
                            ) {
                                Text("StreetView")
                            }
                            Button(
                                onClick = { vm.submitGuess() },
                                enabled = guessPosition != null,
                                modifier = Modifier.padding(1.dp)
                            ) {
                                Text("Tipp bestätigen")
                            }
                        }
                    }

                    GamePhase.Result -> {
                        if (round == totalRounds) {
                            Button(onClick = { vm.nextRound() }) {
                                Text("Beenden")
                            }
                        } else {
                            Button(onClick = { vm.nextRound() }) {
                                Text("Nächste Runde")
                            }
                        }
                    }

                    GamePhase.End -> {
                        println("aaaaa")
                    }
                }
            }
        ) {
            when (phase) {
                GamePhase.StreetView -> {
                    StreetView(
                        streetViewPanoramaOptionsFactory = {
                            StreetViewPanoramaOptions().position(currentLocation?.position)
                        }
                    )
                }

                GamePhase.Guessing -> {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        onMapClick = { latLng: LatLng ->
                            vm.setGuess(latLng)
                        }
                    ) {
                        guessPosition?.let { guess ->
                            Marker(
                                state = MarkerState(position = guess),
                                title = "Dein Tipp"
                            )
                        }
                    }
                }

                GamePhase.Result -> {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = rememberCameraPositionState {
                            position =
                                CameraPosition.fromLatLngZoom(currentLocation!!.position, 10f)
                        }
                    ) {
                        // Spieler-Tipp (blauer Marker)
                        guessPosition?.let { guess ->
                            Marker(
                                state = MarkerState(position = guess),
                                title = "Dein Tipp",
                                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                            )

                            // Polyline zwischen Tipp und richtiger Location
                            Polyline(
                                points = listOf(guess, currentLocation!!.position),
                                color = Color.Red,
                                width = 5f
                            )
                        }

                        // Richtiger Ort (grüner Marker)
                        Marker(
                            state = MarkerState(position = currentLocation!!.position),
                            title = "Richtiger Ort",
                            snippet = currentLocation?.description,
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                        )
                    }
                }

                GamePhase.End -> {
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Spiel beendet!\n\tDein Score: $score",
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Button(onClick = { vm.newGame() }) {
                                Text("Nochmal")

                            }
                            Button(onClick = {
                                LoggedInPlayer.rankedGameStarted = false
                                navController.navigate("mainmenu")
                            }) {
                                Text("Zum Hauptmenü")
                            }
                        }
                    }
                }
            }
        }
    }
    else{
        // Daten sind noch nicht geladen. Wir warten auf die Antwort der DB
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Lade Location...")
        }
    }
}