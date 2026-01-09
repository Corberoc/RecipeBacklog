package com.example.recipebacklog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.recipebacklog.navigation.AppNavHost
import com.example.recipebacklog.ui.theme.RecipeBacklogTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // _____Clément_____
        setContent {
            RecipeBacklogTheme {
                AppNavHost() // _____Clément_____
            }
        }
    }
}
