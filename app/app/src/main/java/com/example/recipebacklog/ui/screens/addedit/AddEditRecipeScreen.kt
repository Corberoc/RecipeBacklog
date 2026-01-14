package com.example.recipebacklog.ui.screens.addedit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipebacklog.model.Recipe
import com.example.recipebacklog.model.RecipeStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRecipeScreen(
    recipe: Recipe? = null,   // si null → ajout, sinon → édition
    onSave: (Recipe) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(recipe?.title ?: "") }
    var description by remember { mutableStateOf(recipe?.description ?: "") }
    var status by remember { mutableStateOf(recipe?.status ?: RecipeStatus.BACKLOG) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (recipe == null) "Add Recipe" else "Edit Recipe") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val newRecipe = Recipe(
                        id = recipe?.id ?: System.currentTimeMillis().toString(),
                        title = title,
                        description = description,
                        status = status
                    )
                    onSave(newRecipe)
                }
            ) {
                Text("Save")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Status")
            RecipeStatus.entries.forEach { s ->
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = status == s,
                        onClick = { status = s }
                    )
                    Text(text = s.name, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}
