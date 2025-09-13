package com.example.gguessr.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gguessr.ui.theme.GguessrTheme
import com.example.gguessr.screens.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.gguessr.viewmodels.StandardGameVM
import com.google.maps.android.ktx.MapsExperimentalFeature


@OptIn(MapsExperimentalFeature::class)
@Composable
fun NavGraph(navController: NavHostController)
{
    NavHost(navController = navController, startDestination = "login")
    {
        // Startpunkt der App: HomeView.ScreenMainMenu() -- Das Hauptmenü
        composable("mainmenu")
        {
            ScreenMainMenu(navController)
        }
        // Singleplayer normal (lokales Spiel)
        composable("standardgame")
        {
            ScreenStandardGame(navController)
        }
        // Singleplayer ranked (Punkte gehen online in die DB)
        composable("rankedstandardgame")
        {
            ScreenStandardGame(navController)
        }
        composable("login")
        {
            ScreenLogin(navController)
        }
        composable("highscores")
        {
            ScreenHighscores()
        }
    }
}
