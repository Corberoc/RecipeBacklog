package com.example.recipebacklog.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebacklog.data.recipes.FirestoreRecipeRepository
import com.example.recipebacklog.data.recipes.RecipeRepository
import com.example.recipebacklog.model.Recipe
import kotlinx.coroutines.launch
import com.example.recipebacklog.data.analytics.AnalyticsLogger

class HomeViewModel(
    private val repo: RecipeRepository = FirestoreRecipeRepository()
) : ViewModel() {

    var recipes by mutableStateOf<List<Recipe>>(emptyList())
        private set

    fun loadRecipes() {
        viewModelScope.launch {
            recipes = repo.getAll()
        }
    }

    fun importRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repo.upsert(recipe)
            AnalyticsLogger.logRecipeImported(recipe.id)
            recipes = repo.getAll()
        }
    }

    fun addOrUpdateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repo.upsert(recipe)
            AnalyticsLogger.logRecipeSaved(recipe.id)
            recipes = repo.getAll()
        }
    }

    fun deleteRecipe(recipeId: String) {
        viewModelScope.launch {
            repo.delete(recipeId)
            recipes = repo.getAll()
        }
    }
}


