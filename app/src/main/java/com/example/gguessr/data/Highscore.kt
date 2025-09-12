package com.example.gguessr.data

data class Highscore(
    val date: String = "dd-mm-yyyy",
    val time: String = "00:00:00",
    val who: String = " ",
    val playerId: String = "id",
    val score: Int = 0
)