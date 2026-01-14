package com.example.recipebacklog.model

data class Recipe(
    val id: String = "",
    val title: String = "",
    val description: String? = null,
    val imageUrl: String? = null,
    val status: RecipeStatus = RecipeStatus.BACKLOG,
    val isFavorite: Boolean = false
)
