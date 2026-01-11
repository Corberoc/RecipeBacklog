package com.example.recipebacklog.domain.models_old

data class Recipe(
    val id: String,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val status: RecipeStatus
)

enum class RecipeStatus {
    BACKLOG, TODO, DONE
}
