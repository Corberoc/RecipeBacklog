package com.example.recipebacklog.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Orange,
    secondary = DarkBlue,
    background = Color.White,
    surface = Color.White,
    surfaceVariant = LightCream,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DarkBlue,
    onSurface = DarkBlue,
    onSurfaceVariant = DarkBlue,
    outline = Grey
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
