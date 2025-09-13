package com.example.gguessr.viewmodels

import androidx.lifecycle.ViewModel
import com.example.gguessr.data.Database
import com.example.gguessr.data.Highscore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HighscoresVM: ViewModel(){
    private val _highscores = MutableStateFlow<List<Highscore>>(emptyList())
    val highscores = _highscores.asStateFlow()
    init {
        Database.getHighscores { hs ->
            _highscores.value = hs
        }
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        println(LocalDate.now().format(formatter))
    }
}