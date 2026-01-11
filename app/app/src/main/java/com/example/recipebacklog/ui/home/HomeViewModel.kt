/*package com.example.recipebacklog.ui.home // _____Clément_____

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
}*/
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
            repo.upsert(recipe)     // ✅ Firestore
            recipes = repo.getAll() // ✅ refresh
        }
    }

    fun addOrUpdateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repo.upsert(recipe)
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


