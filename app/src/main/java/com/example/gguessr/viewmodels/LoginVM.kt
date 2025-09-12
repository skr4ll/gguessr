package com.example.gguessr.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.gguessr.data.Database
import com.example.gguessr.data.Highscore
import com.example.gguessr.data.LoggedInPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginVM : ViewModel() {

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

//    private val _playerName = MutableStateFlow<String?>(null)
//    val playerName = _playerName.asStateFlow()
     fun attemptLogin(name: String, password: String) {
        Database.loginPlayer(name, password) { success ->
            if (success) {
                _loginSuccess.value = true
                // Spielername global speichern
                LoggedInPlayer.playerName = name
            } else {
                _loginSuccess.value = false
            }
        }
    }
}
