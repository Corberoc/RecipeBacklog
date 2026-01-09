package com.example.recipebacklog.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipebacklog.data.api.models.MealApiRepository // _____Clément_____
import com.example.recipebacklog.data.api.models.ApiMeal
import com.example.recipebacklog.domain.models.Recipe
import com.example.recipebacklog.domain.mappers.toRecipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen( // _____Clément_____
    mealId: String,
    repository: MealApiRepository = MealApiRepository(),
    onImport: (Recipe) -> Unit,
    onBack: () -> Unit // _____Clément_____
) {
    var meal by remember { mutableStateOf<ApiMeal?>(null) }

    LaunchedEffect(mealId) {
        meal = repository.getMealDetail(mealId)
    }

    Scaffold( // _____Clément_____
        topBar = {
            TopAppBar(
                title = { Text(meal?.strMeal ?: "Détails") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            meal?.let { m ->
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = m.strMeal,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(text = m.strInstructions ?: "Aucune instruction disponible")
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = { onImport(m.toRecipe()) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Importer")
                    }
                }
            } ?: run {
                Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
