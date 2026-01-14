package com.example.recipebacklog.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipebacklog.model.Recipe
import com.example.recipebacklog.model.RecipeStatus
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onRecipeClick: (String) -> Unit,
    onAccountClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    var selectedStatus by remember { mutableStateOf(RecipeStatus.BACKLOG) }
    var showFavoritesOnly by remember { mutableStateOf(false) }

    val filteredRecipes = viewModel.recipes.let { list ->
        if (showFavoritesOnly) list.filter { it.isFavorite }
        else list.filter { it.status == selectedStatus }
    }

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
            FloatingActionButton(onClick = onSearchClick) {
                Icon(Icons.Default.Search, contentDescription = "Rechercher")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // Filtres
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = showFavoritesOnly,
                    onClick = { showFavoritesOnly = true },
                    label = { Text("⭐ Favoris") }
                )

                RecipeStatus.entries.forEach { status ->
                    FilterChip(
                        selected = !showFavoritesOnly && selectedStatus == status,
                        onClick = {
                            showFavoritesOnly = false
                            selectedStatus = status
                        },
                        label = { Text(status.displayName) }
                    )
                }
            }

            if (filteredRecipes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("Aucune recette dans cette catégorie.")
                }
            } else {
                // Affichage adaptatif
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 300.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredRecipes) { recipe ->
                        RecipeItem(
                            recipe = recipe,
                            onClick = { onRecipeClick(recipe.id) },
                            onToggleFavorite = { viewModel.toggleFavorite(recipe) },
                            onDelete = { viewModel.deleteRecipe(recipe.id) },
                            onChangeStatus = { newStatus ->
                                viewModel.changeStatus(recipe.id, newStatus)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeItem(
    recipe: Recipe,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit,
    onChangeStatus: (RecipeStatus) -> Unit
) {
    var statusMenuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ✅ Miniature image (si imageUrl existe)
            if (!recipe.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(recipe.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Image de ${recipe.title}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
            }

            Column(modifier = Modifier.weight(1f)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row {
                        IconButton(onClick = onToggleFavorite) {
                            Icon(
                                imageVector = if (recipe.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = "Favori"
                            )
                        }
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Filled.Delete, contentDescription = "Supprimer")
                        }
                    }
                }

                recipe.description?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Statut : ${recipe.status.displayName}")

                    TextButton(onClick = { statusMenuExpanded = true }) {
                        Text("Changer")
                    }

                    DropdownMenu(
                        expanded = statusMenuExpanded,
                        onDismissRequest = { statusMenuExpanded = false }
                    ) {
                        RecipeStatus.entries.forEach { s ->
                            DropdownMenuItem(
                                text = { Text(s.displayName) },
                                onClick = {
                                    statusMenuExpanded = false
                                    onChangeStatus(s)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
