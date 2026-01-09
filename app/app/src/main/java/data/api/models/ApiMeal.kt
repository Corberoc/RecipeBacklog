package com.example.recipebacklog.data.api.models // _____Clément_____

import kotlinx.serialization.Serializable

@Serializable
data class ApiMeal(
    val idMeal: String,
    val strMeal: String,
    val strInstructions: String? = null,
    val strMealThumb: String? = null
)
