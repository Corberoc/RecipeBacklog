package com.example.recipebacklog.data.recipes

import com.example.recipebacklog.model.Recipe
import com.example.recipebacklog.model.RecipeStatus

interface RecipeRepository {
    suspend fun getAll(): List<Recipe>
    suspend fun upsert(recipe: Recipe) // créer ou mettre à jour
    suspend fun delete(recipeId: String)
    suspend fun updateStatus(recipeId: String, status: RecipeStatus)
    suspend fun updateFavorite(recipeId: String, isFavorite: Boolean)
}
