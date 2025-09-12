package com.example.gguessr.data

data class Player(
    val id: String,
    val name: String
)

// Dieser String erhält den Namen des Spielers, der sich erfolgreich gegen die Datenbank eingeloggt hat
object LoggedInPlayer {
    var playerName: String? = null
}
