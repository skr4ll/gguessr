package com.example.gguessr.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gguessr.viewmodels.StandardGameVM
import com.google.android.gms.maps.StreetViewPanoramaOptions
import com.google.maps.android.compose.streetview.StreetView
import com.google.maps.android.ktx.MapsExperimentalFeature

@OptIn(MapsExperimentalFeature::class)
@MapsExperimentalFeature
@Composable

fun ScreenStandardGame(navController: NavController) {
    val viewModel: StandardGameVM = viewModel()
    val position by viewModel.position.collectAsState()
    val cameraPositionState by viewModel.cameraPositionState.collectAsState()

    StreetView(
        cameraPositionState = cameraPositionState,
        streetViewPanoramaOptionsFactory = {
            StreetViewPanoramaOptions().position(position)
        },
    )
}
