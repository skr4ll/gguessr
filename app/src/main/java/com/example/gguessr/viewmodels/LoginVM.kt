package com.example.gguessr.viewmodels

import androidx.lifecycle.ViewModel
import com.example.gguessr.data.Database
import com.example.gguessr.data.DatabaseLocation
import com.example.gguessr.data.Location
import com.example.gguessr.data.LoggedInPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginVM : ViewModel() {

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

    private val _loginMessage = MutableStateFlow("")
    val loginMessage = _loginMessage.asStateFlow()

     fun attemptLogin(name: String, password: String, onLoginResult: (Boolean) -> Unit) {
        Database.loginPlayer(name, password) { success ->
            if (success) {
                _loginMessage.value = "Willkommen $name"
                Database.getPlayersHighscore(LoggedInPlayer.playerId) { score ->
                    onLoginResult(true)
                    if (score != null) {
                        LoggedInPlayer.currentHighscore = score
                    }
                    else{
                        LoggedInPlayer.currentHighscore = 0
                    }
                }
            } else {
                _loginMessage.value = "Falsche Daten!"
                onLoginResult(false)
            }
        }
    }
}
