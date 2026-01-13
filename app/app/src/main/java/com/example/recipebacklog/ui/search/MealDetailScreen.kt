package com.example.recipebacklog.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipebacklog.data.api.models.MealApiRepository
import com.example.recipebacklog.data.api.models.ApiMeal
import com.example.recipebacklog.model.Recipe
import com.example.recipebacklog.domain.mappers.toRecipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(
    mealId: String,
    repository: MealApiRepository = MealApiRepository(),
    onImport: (Recipe) -> Unit,
    onBack: () -> Unit
) {
    var meal by remember { mutableStateOf<ApiMeal?>(null) }

    LaunchedEffect(mealId) {
        meal = repository.getMealDetail(mealId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(meal?.strMeal ?: "Détails") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
        bottomBar = {
            // ✅ Bouton TOUJOURS visible
            meal?.let { m ->
                Button(
                    onClick = { onImport(m.toRecipe()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Importer")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {

            meal?.let { m ->
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = m.strMeal,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = m.strInstructions ?: "Aucune instruction disponible",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    // ✅ Espace pour éviter que le texte soit caché par le bouton
                    Spacer(Modifier.height(80.dp))
                }
            } ?: run {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
