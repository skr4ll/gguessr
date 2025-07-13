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


@Composable
fun NavGraph(navController: NavHostController)
{
    NavHost(navController = navController, startDestination = "mainmenu")
    {
        // Startpunkt der App: ScreenMainMenu() -- Das Hauptmenü
            composable("mainmenu")
            {
                ScreenMainMenu(navController)
            }

    }
}
