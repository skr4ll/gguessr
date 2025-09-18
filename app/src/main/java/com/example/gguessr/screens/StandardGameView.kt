package com.example.gguessr.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
    val deviation by vm.deviation.collectAsState()
    val currentLocation by vm.currentLocation.collectAsState()
    val guessPosition by vm.guessPosition.collectAsState()
    val timeLeft by vm.timeLeft.collectAsState()

    if (currentLocation != null) {
        // Back-Button abfangen
        BackHandler {
            vm.resetGameVM()
            navController.navigate("mainmenu") {
                popUpTo("mainmenu") { inclusive = true }
            }
        }

        Scaffold(
            topBar = {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopAppBar(
                        title = {
                            Text("$round/$totalRounds Runden - Score: $score - Abweichung: $deviation km")
                        }
                    )
                    if (LoggedInPlayer.timedGameStarted) {
                        Text(
                            text = "Zeit: ${timeLeft}s",
                            style = MaterialTheme.typography.titleLarge,
                            color = if (timeLeft <= 10) Color.Red else Color.Green
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                when (phase) {
                    GamePhase.StreetView -> {
                        StreetView(
                            streetViewPanoramaOptionsFactory = {
                                StreetViewPanoramaOptions().position(currentLocation?.position)
                                    .streetNamesEnabled(false)
                            },
                            modifier = Modifier.fillMaxSize()
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 100.dp), // Abstand nach oben
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(onClick = { vm.startGuessing() }) {
                                Text("Ort tippen")
                            }
                        }
                    }

                    GamePhase.Guessing -> {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            onMapClick = { latLng -> vm.setGuess(latLng) }
                        ) {
                            guessPosition?.let { guess ->
                                Marker(state = MarkerState(position = guess), title = "Dein Tipp")
                            }
                        }

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 100.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(onClick = { vm.returnToStreetView() }) {
                                    Text("StreetView")
                                }
                                Button(
                                    onClick = { vm.submitGuess() },
                                    enabled = guessPosition != null
                                ) {
                                    Text("Tipp bestätigen")
                                }
                            }
                        }
                    }

                    GamePhase.Result -> {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(
                                    currentLocation!!.position,
                                    10f
                                )
                            }
                        ) {
                            guessPosition?.let { guess ->
                                Marker(
                                    state = MarkerState(position = guess),
                                    title = "Dein Tipp",
                                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                                )

                                Polyline(
                                    points = listOf(guess, currentLocation!!.position),
                                    color = Color.Red,
                                    width = 5f
                                )
                            }

                            Marker(
                                state = MarkerState(position = currentLocation!!.position),
                                title = "Richtiger Ort",
                                snippet = currentLocation?.description,
                                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 100.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(onClick = { vm.nextRound() }) {
                                Text(if (round == totalRounds) "Beenden" else "Nächste Runde")
                            }
                        }
                    }

                    GamePhase.End -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.DarkGray),
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
                                    vm.resetGameVM()
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
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Lade Location...")
        }
    }
}