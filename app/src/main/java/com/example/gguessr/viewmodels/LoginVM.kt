package com.example.gguessr.viewmodels

import androidx.lifecycle.ViewModel
import com.example.gguessr.data.Database
import com.example.gguessr.data.DatabaseLocation
import com.example.gguessr.data.Location
import com.example.gguessr.data.LoggedInPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginVM : ViewModel() {
     fun attemptLogin(name: String, password: String, onLoginResult: (Boolean) -> Unit) {
        Database.loginPlayer(name, password) { success ->
            if (success) {
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
                onLoginResult(false)
            }
        }
    }
}
