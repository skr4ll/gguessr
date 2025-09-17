package com.example.gguessr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.gguessr.navigation.NavGraph
import com.example.gguessr.ui.theme.GguessrTheme

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            GguessrTheme{
                Surface{
                    AppRoot()
                }
            }
        }
    }
}

@Composable
fun AppRoot(){
    val nc = rememberNavController()
    NavGraph(navController = nc)
}