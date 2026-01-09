package com.example.recipebacklog.ui.home // _____Clément_____

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
import androidx.compose.material.icons.filled.Search
import com.example.recipebacklog.domain.models.Recipe
import com.example.recipebacklog.domain.models.RecipeStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen( // _____Clément_____
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onRecipeClick: (String) -> Unit, // _____Clément_____
    onAccountClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    var selectedStatus by remember { mutableStateOf(RecipeStatus.BACKLOG) } // _____Clément_____
    
    // On filtre les recettes du ViewModel par le statut sélectionné
    val filteredRecipes = viewModel.recipes.filter { it.status == selectedStatus }

    val screenWidth = LocalConfiguration.current.screenWidthDp

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mes Recettes") },
                actions = {
                    IconButton(onClick = onAccountClick) {
                        Icon(Icons.Default.Person, contentDescription = "Account")
                    }
                    IconButton(onClick = onAboutClick) {
                        Icon(Icons.Default.Info, contentDescription = "About")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onSearchClick) { // _____Clément_____
                Icon(Icons.Default.Search, contentDescription = "Rechercher")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // Filtre des status (Backlog, Favorite, etc.)
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

            if (filteredRecipes.isEmpty()) { // _____Clément_____
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Aucune recette dans cette catégorie.")
                }
            } else {
                // Affichage adaptatif (Liste sur téléphone, Grille sur tablette)
                if (screenWidth < 600) {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                        items(filteredRecipes) { recipe ->
                            RecipeItem(recipe = recipe, onClick = { onRecipeClick(recipe.id) }) // _____Clément_____
                            HorizontalDivider()
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredRecipes) { recipe ->
                            RecipeItem(recipe = recipe, onClick = { onRecipeClick(recipe.id) }) // _____Clément_____
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe, onClick: () -> Unit) { // _____Clément_____
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(recipe.title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            recipe.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
            }
        }
    }
}
