package com.example.gguessr.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gguessr.viewmodels.GamePhase
import com.example.gguessr.viewmodels.StandardGameVM
import com.example.gguessr.viewmodels.totalRounds
import com.google.android.gms.maps.StreetViewPanoramaOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.maps.android.compose.streetview.StreetView
import com.google.maps.android.ktx.MapsExperimentalFeature

@Composable
@Preview
fun ScoreScreen(){
    Text("sdfsdf")
}

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

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Runde $round/$totalRounds --- Score: $score") })
        },
        bottomBar = {
            when (phase) {
                GamePhase.StreetView -> {
                    Button(onClick = { vm.startGuessing() }) {
                        Text("Ort tippen")
                    }
                }
                GamePhase.Guessing -> {
                    Button(
                        onClick = { vm.submitGuess() },
                        enabled = guessPosition != null
                    ) {
                        Text("Tipp bestätigen")
                    }
                }
                GamePhase.Result -> {
                    if (round == totalRounds){
                        Button(onClick = { vm.nextRound() }) {
                            Text("Beenden")
                        }
                    }
                    else{
                        Button(onClick = { vm.nextRound() }) {
                            Text("Nächste Runde")
                        }
                    }
                }

                GamePhase.End -> {
                    Button(onClick = { navController.navigate("mainmenu") }) {
                        Text("Zum Hauptmenü")
                    }
                }
            }
        }
    ) {
        when (phase) {
            GamePhase.StreetView -> {
                StreetView(
                    streetViewPanoramaOptionsFactory = {
                        StreetViewPanoramaOptions().position(currentLocation.position)
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
                        position = CameraPosition.fromLatLngZoom(currentLocation.position, 10f)
                    }
                ) {
                    // Spieler-Tipp (blauer Marker)
                    guessPosition?.let { guess ->
                        Marker(
                            state = MarkerState(position = guess),
                            title = "Dein Tipp",
                            icon = BitmapDescriptorFactory.
                                defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                        )

                        // Polyline zwischen Tipp und richtiger Location
                        Polyline(
                            points = listOf(guess, currentLocation.position),
                            color = Color.Red,
                            width = 5f
                        )
                    }

                    // Richtiger Ort (grüner Marker)
                    Marker(
                        state = MarkerState(position = currentLocation.position),
                        title = "Richtiger Ort",
                        snippet = currentLocation.name,
                        icon = BitmapDescriptorFactory.
                            defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                    )
                }
            }
            GamePhase.End -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Spiel beendet!\nDein Score: $score",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}