package com.example.recipebacklog.data.api.models // _____Clément_____

import kotlinx.serialization.Serializable

@Serializable
data class MealResponse(
    val meals: List<ApiMeal>? = null
)
