package com.example.recipebacklog.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import com.example.recipebacklog.model.Recipe
import com.example.recipebacklog.model.RecipeStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    recipes: List<Recipe> = mockRecipes,
    onAdd: () -> Unit,
    onRecipeClick: (String) -> Unit,
    onAccount: () -> Unit,
    onAbout: () -> Unit
) {
    var selectedStatus by remember { mutableStateOf(RecipeStatus.BACKLOG) }
    val filtered = recipes.filter { it.status == selectedStatus }

    val screenWidth = LocalConfiguration.current.screenWidthDp

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipes") },
                actions = {
                    IconButton(onClick = { onAccount() }) {
                        Icon(Icons.Default.Person, contentDescription = "Account")
                    }
                    IconButton(onClick = { onAbout() }) {
                        Icon(Icons.Default.Info, contentDescription = "About")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // Filtre des status
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RecipeStatus.entries.forEach { status ->
                    FilterChip(
                        selected = selectedStatus == status,
                        onClick = { selectedStatus = status },
                        label = { Text(status.name) }
                    )
                }
            }

            // Liste responsive
            if (screenWidth < 600) {
                // Phone
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filtered) { recipe ->
                        Text(
                            text = recipe.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onRecipeClick(recipe.id) }
                                .padding(16.dp)
                        )
                        HorizontalDivider()
                    }
                }
            } else {
                // Tablet / large
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filtered) { recipe ->
                        Text(
                            text = recipe.title,
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable { onRecipeClick(recipe.id) }
                        )
                    }
                }
            }
        }
    }
}

// Mock data
val mockRecipes = listOf(
    Recipe("1", "Pâtes Carbo", "Classique",null, RecipeStatus.BACKLOG),
    Recipe("2", "Burger", "Steak + cheddar", null,RecipeStatus.IN_PROGRESS),
    Recipe("3", "Curry", "Épicé", null,RecipeStatus.DONE),
    Recipe("4", "Salade", "Légère", null,RecipeStatus.BACKLOG),
    Recipe("5", "Tacos", "Rapide", null,RecipeStatus.IN_PROGRESS)
)
