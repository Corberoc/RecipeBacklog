package com.example.recipebacklog.domain.mappers

import com.example.recipebacklog.data.api.models.ApiMeal
import com.example.recipebacklog.model.Recipe
import com.example.recipebacklog.model.RecipeStatus

fun ApiMeal.toRecipe(): Recipe {
    return Recipe(
        id = idMeal,
        title = strMeal,
        description = strInstructions,
        imageUrl = strMealThumb,
        status = RecipeStatus.BACKLOG
    )
}
