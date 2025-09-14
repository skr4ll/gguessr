package com.example.gguessr.viewmodels

import androidx.lifecycle.ViewModel
import com.example.gguessr.data.Database
import com.example.gguessr.data.LoggedInPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterVM: ViewModel() {
    fun attemptRegister(name: String, password: String, onRegisterResult: (Boolean) -> Unit) {
        Database.createPlayer(name, password) {  success ->
            if(success){
                onRegisterResult(true)
                // Neuer Spieler, also muss Highscore = 0 sein
                LoggedInPlayer.currentHighscore = 0
            }
            else{
                onRegisterResult(false)
            }
        }
    }
}
