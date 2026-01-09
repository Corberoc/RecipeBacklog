package com.example.recipebacklog.ui.search

import android.util.Log // _____Clément_____
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebacklog.data.api.models.MealApiRepository // _____Clément_____
import com.example.recipebacklog.data.api.models.ApiMeal
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class SearchViewModel(
    private val repository: MealApiRepository = MealApiRepository()
) : ViewModel() {

    var query by mutableStateOf("") // _____Clément_____
    var results by mutableStateOf<List<ApiMeal>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun search() {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                results = repository.searchMeals(query)

                if (results.isEmpty()) {
                    errorMessage = "Aucune recette trouvée"
                }

            } catch (e: Exception) {
                errorMessage = "Erreur réseau" // _____Clément_____
                Log.e("SearchViewModel", "Erreur réseau", e) // _____Clément_____
            } finally {
                isLoading = false
            }
        }
    }
}
