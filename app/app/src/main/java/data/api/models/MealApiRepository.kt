package com.example.recipebacklog.data.api.models // _____Clément_____

import com.example.recipebacklog.data.api.ApiClient // _____Clément_____
import io.ktor.client.call.body
import io.ktor.client.request.get

class MealApiRepository {

    suspend fun searchMeals(query: String): List<ApiMeal> {
        val response: MealResponse = ApiClient.client.get(
            "https://www.themealdb.com/api/json/v1/1/search.php?s=$query"
        ).body()

        return response.meals ?: emptyList()
    }

    suspend fun getMealDetail(id: String): ApiMeal? {
        val response: MealResponse = ApiClient.client.get(
            "https://www.themealdb.com/api/json/v1/1/lookup.php?i=$id"
        ).body()

        return response.meals?.firstOrNull()
    }
}
