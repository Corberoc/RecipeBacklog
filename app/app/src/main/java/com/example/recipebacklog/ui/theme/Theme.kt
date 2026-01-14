package com.example.recipebacklog.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = DarkBlue,
    background = Cream,
    surface = LightCream,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DarkBlue,
    onSurface = DarkBlue,
)

@Composable
fun RecipeBacklogTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}