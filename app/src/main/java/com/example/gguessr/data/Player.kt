package com.example.gguessr.data

data class Player(
    val name: String,
    val password: String
)

// Dieser String erhält den Namen und die ID des Spielers, der sich erfolgreich gegen die Datenbank eingeloggt hat.
// Zudem finden sich hier weitere globale Kontrollvariablen
object LoggedInPlayer {
    var playerName: String? = null
    var playerId: String = ""
    var currentHighscore = 0
    var rankedGameStarted: Boolean = false
    var timedGameStarted: Boolean = false
}
