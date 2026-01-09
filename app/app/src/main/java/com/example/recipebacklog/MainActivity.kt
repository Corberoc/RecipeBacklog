package com.example.recipebacklog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import com.example.recipebacklog.ui.navigation.AppNavGraph
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MaterialTheme {
                // On initialise le contrôleur de navigation ici
                val navController = rememberNavController()
                // On passe le contrôleur à notre graphe de navigation
                AppNavGraph(navController = navController)
            }
        }
    }
}
