package com.example.recipebacklog.domain.mappers

import com.example.recipebacklog.data.api.models.ApiMeal
import com.example.recipebacklog.model.Recipe
import com.example.recipebacklog.model.RecipeStatus

//import com.example.recipebacklog.domain.models_old.Recipe
//import com.example.recipebacklog.domain.models_old.RecipeStatus // _____Clément_____

fun ApiMeal.toRecipe(): Recipe { // _____Clément_____
    return Recipe(
        id = idMeal,
        title = strMeal,
        description = strInstructions,
        imageUrl = strMealThumb,
        status = RecipeStatus.BACKLOG
    )
}
