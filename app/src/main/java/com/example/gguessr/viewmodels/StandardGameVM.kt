package com.example.gguessr.viewmodels

import androidx.lifecycle.ViewModel
import com.example.gguessr.data.LocationRepository

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.streetview.StreetViewCameraPositionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class GamePhase { StreetView, Guessing, Result, End }
const val totalRounds = 1

class StandardGameVM () : ViewModel()
{

    private val _phase = MutableStateFlow(GamePhase.StreetView)
    val phase = _phase.asStateFlow()

    private val _round = MutableStateFlow(1)
    val round = _round.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val locations = LocationRepository.locations
    private var currentIndex = 0

    private val _currentLocation = MutableStateFlow(locations[currentIndex])
    val currentLocation = _currentLocation.asStateFlow()

    private val _guessPosition = MutableStateFlow<LatLng?>(null)
    val guessPosition = _guessPosition.asStateFlow()

    fun startGuessing() {
        _phase.value = GamePhase.Guessing
    }

    fun setGuess(latLng: LatLng) {
        _guessPosition.value = latLng
    }

    fun submitGuess() {
        val guess = _guessPosition.value ?: return
        val actual = _currentLocation.value.position
        val points = 100
        _score.value += points
        _phase.value = GamePhase.Result
    }

    fun nextRound() {
        if (currentIndex < totalRounds - 1) {
            currentIndex++
            _currentLocation.value = locations[currentIndex]
            _guessPosition.value = null
            _phase.value = GamePhase.StreetView
            _round.value = currentIndex + 1
        } else {
            // Spielende
            _phase.value = GamePhase.End
        }
    }
}

//    private val _position = MutableStateFlow(LatLng(50.92894992179465, 6.927461350615642)) // Uni
//    val position = _position.asStateFlow()
//
//    private val _cameraPositionState = MutableStateFlow(StreetViewCameraPositionState())
//    val cameraPositionState = _cameraPositionState.asStateFlow()
//
//    fun updatePosition(latLng: LatLng) {
//        _position.value = latLng
//    }