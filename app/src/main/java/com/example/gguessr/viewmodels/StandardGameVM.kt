package com.example.gguessr.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.gguessr.data.Database
import com.example.gguessr.data.Location
import com.example.gguessr.data.LoggedInPlayer
import com.example.gguessr.util.Utils.CalculateScore
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

enum class GamePhase { StreetView, Guessing, Result, End }
const val totalRounds = 5

class StandardGameVM : ViewModel() {

    private var locations: MutableList<Location> = mutableListOf() // veränderbare Liste für Spiel
    private var currentIndex: Int = 0

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    private val _phase = MutableStateFlow(GamePhase.StreetView)
    val phase = _phase.asStateFlow()

    private val _round = MutableStateFlow(1)
    val round = _round.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val _guessPosition = MutableStateFlow<LatLng?>(null)
    val guessPosition = _guessPosition.asStateFlow()

    private val totalRounds = 5

    init {
        // Locations aus Datenbank laden
        Database.rewriteGetLocations { locs ->
            locations = locs.toMutableList()
            if (locations.isNotEmpty()) {
                currentIndex = Random.nextInt(locations.size)
                _currentLocation.value = locations[currentIndex]
            }
        }
        Log.i("--> LOCATION-INFO", "Loaded Locations: $locations")
        Log.i("--> PLAYER-INFO", "The player: \"${LoggedInPlayer.playerName}\" is logged in")
    }

    fun startGuessing() {
        _phase.value = GamePhase.Guessing
    }

    fun returnToStreetView() {
        _phase.value = GamePhase.StreetView
    }

    fun setGuess(latLng: LatLng) {
        _guessPosition.value = latLng
    }

    fun submitGuess() {
        val guess = _guessPosition.value ?: return
        val actual = _currentLocation.value?.position ?: return
        val points = CalculateScore.calculateScore(guess, actual)
        _score.value += points
        _phase.value = GamePhase.Result
    }

    fun nextRound() {
        if (_round.value < totalRounds && locations.isNotEmpty()) {
            // Aktuelle Location aus der Liste entfernen
            locations.removeAt(currentIndex)
            // Neue Location zufällig wählen
            if (locations.isNotEmpty()) {
                currentIndex = Random.nextInt(locations.size)
                _currentLocation.value = locations[currentIndex]
            }
            _guessPosition.value = null
            _phase.value = GamePhase.StreetView
            _round.value++
        } else {
            _phase.value = GamePhase.End
        }
    }

    fun newGame() {
        Database.rewriteGetLocations { locs ->
            locations = locs.toMutableList()
            currentIndex = Random.nextInt(locations.size)
            _currentLocation.value = locations[currentIndex]

            _phase.value = GamePhase.StreetView
            _score.value = 0
            _round.value = 1
            _guessPosition.value = null
        }
    }
}