package com.example.gguessr.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gguessr.data.Database
import com.example.gguessr.data.Highscore
import com.example.gguessr.data.Location
import com.example.gguessr.data.LoggedInPlayer
import com.example.gguessr.util.Utils.CalculateScore
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
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

    private val _deviation = MutableStateFlow(0.0)
    val deviation = _deviation.asStateFlow()

    private val _guessPosition = MutableStateFlow<LatLng?>(null)
    val guessPosition = _guessPosition.asStateFlow()

    private val _timeLeft = MutableStateFlow(60) // Start bei 60 Sekunden
    val timeLeft = _timeLeft.asStateFlow()

    private var timerJob: Job? = null

    private val totalRounds = 5

    init {
        // Locations aus Datenbank laden
        Database.getLocations { locs ->
            locations = locs.toMutableList()
            if (locations.isNotEmpty()) {
                currentIndex = Random.nextInt(locations.size)
                _currentLocation.value = locations[currentIndex]
                if (LoggedInPlayer.timedGameStarted){
                    startTimer()
                }
            }
        }
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
        // Erhalte und summiere die Abweichungen zwischen Tipp und richtiger Location
        _deviation.value += CalculateScore.distanceInKm(guess, actual)
        // Schnelles runden auf 2 Nachkommastellen
        _deviation.value = (_deviation.value * 100).roundToInt() / 100.0
        _score.value += points
        _phase.value = GamePhase.Result
    }

    fun compareAndUpdateHighscore(){
        if (score.value > LoggedInPlayer.currentHighscore){
            var gameType = "normal"
            if(LoggedInPlayer.timedGameStarted){ gameType = "timed" }
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            val newHS = Highscore(
                LocalDate.now().format(dateFormatter),
                LocalTime.now().format(timeFormatter),
                LoggedInPlayer.playerName.toString(),
                score.value,
                gameType
                )
            Database.createOrUpdateHighscore(newHS, LoggedInPlayer.playerId)
        }
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
            if (LoggedInPlayer.rankedGameStarted){
                compareAndUpdateHighscore()
            }
        }
    }

    fun newGame() {
        Database.getLocations { locs ->
            locations = locs.toMutableList()
            currentIndex = Random.nextInt(locations.size)
            _currentLocation.value = locations[currentIndex]

            _phase.value = GamePhase.StreetView
            _score.value = 0
            _round.value = 1
            _guessPosition.value = null
            if (LoggedInPlayer.timedGameStarted) {
                startTimer()
            }
        }
    }
    fun startTimer() {
        // Falls schon ein Timer läuft, abbrechen
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            _timeLeft.value = 60
            while (_timeLeft.value > 0) {
                delay(1000L)
                _timeLeft.value = _timeLeft.value - 1
            }
            // Zeit abgelaufen → Spiel beenden
            _phase.value = GamePhase.End
            if (LoggedInPlayer.timedGameStarted) {
                compareAndUpdateHighscore()
            }
        }
    }
    fun stopTimer() {
        timerJob?.cancel()
    }
}