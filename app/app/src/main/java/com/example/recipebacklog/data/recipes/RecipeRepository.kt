package com.example.recipebacklog.data.recipes

import com.example.recipebacklog.model.Recipe
import com.example.recipebacklog.model.RecipeStatus

interface RecipeRepository {
    suspend fun getAll(): List<Recipe>
    suspend fun upsert(recipe: Recipe) // create or update
    suspend fun delete(recipeId: String)
    suspend fun updateStatus(recipeId: String, status: RecipeStatus)
}
