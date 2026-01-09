package com.example.recipebacklog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.recipebacklog.navigation.AppNavHost
import com.example.recipebacklog.ui.theme.RecipeBacklogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeBacklogTheme {
                AppNavHost()   // 👉 On lance directement la navigation
            }
        }
    }
}
