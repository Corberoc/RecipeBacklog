package com.example.recipebacklog.ui.home // _____Clément_____

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.recipebacklog.domain.models.Recipe

class HomeViewModel : ViewModel() { // _____Clément_____

    var recipes by mutableStateOf<List<Recipe>>(emptyList()) // _____Clément_____
        private set

    fun importRecipe(recipe: Recipe) { // _____Clément_____
        recipes = recipes + recipe
    }
}
