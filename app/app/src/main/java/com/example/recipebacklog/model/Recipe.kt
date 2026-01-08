package com.example.recipebacklog.model

data class Recipe(
    val id: String,
    val title: String,
    val description: String,
    val status: RecipeStatus
)