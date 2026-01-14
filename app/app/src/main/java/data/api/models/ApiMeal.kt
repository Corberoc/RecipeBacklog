package com.example.recipebacklog.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiMeal(
    val idMeal: String,
    val strMeal: String,
    val strInstructions: String? = null,
    val strMealThumb: String? = null
)
