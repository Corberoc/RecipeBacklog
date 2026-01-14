package com.example.recipebacklog.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipebacklog.model.Recipe
import com.example.recipebacklog.model.RecipeStatus
import com.example.recipebacklog.ui.theme.DarkBlue
import com.example.recipebacklog.ui.theme.LightCream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    recipes: List<Recipe> = emptyList(),
    onAdd: () -> Unit,
    onRecipeClick: (String) -> Unit,
    onAccount: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<RecipeStatus?>(null) }

    val filteredRecipes = recipes.filter { recipe ->
        val matchesSearch = recipe.title.contains(searchQuery, ignoreCase = true)
        val matchesStatus = selectedStatus?.let { it == recipe.status } ?: true
        matchesSearch && matchesStatus
    }

    Scaffold(
        topBar = { TopBar(onAccount) },
        modifier = Modifier.background(Color.White)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            StatsCards(recipes = recipes)

            Spacer(modifier = Modifier.height(24.dp))

            SearchBar(value = searchQuery, onValueChange = { searchQuery = it })

            Spacer(modifier = Modifier.height(16.dp))

            FilterActions(selectedStatus = selectedStatus, onStatusSelected = { selectedStatus = it }, onAdd = onAdd)

            if (filteredRecipes.isEmpty()) {
                EmptyState(onAdd = onAdd)
            } else {
                RecipeList(recipes = filteredRecipes, onRecipeClick = onRecipeClick)
            }
        }
    }
}

@Composable
fun TopBar(onAccount: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(LightCream, CircleShape)
            ) // Placeholder for avatar image
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Mes Recettes", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkBlue)
                Text("Bienvenue, oui", fontSize = 14.sp, color = Color.Gray)
            }
        }
        IconButton(onClick = onAccount) {
            Icon(Icons.Default.Person, contentDescription = "Account", tint = Color.Gray)
        }
    }
}

@Composable
fun StatsCards(recipes: List<Recipe>) {
    val total = recipes.size
    val todo = recipes.count { it.status == RecipeStatus.BACKLOG }
    val inProgress = recipes.count { it.status == RecipeStatus.IN_PROGRESS }
    val done = recipes.count { it.status == RecipeStatus.DONE }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatCard("Total", total.toString(), Modifier.weight(1f))
        Spacer(modifier = Modifier.width(8.dp))
        StatCard(RecipeStatus.BACKLOG.displayName, todo.toString(), Modifier.weight(1f))
        Spacer(modifier = Modifier.width(8.dp))
        StatCard(RecipeStatus.IN_PROGRESS.displayName, inProgress.toString(), Modifier.weight(1f))
        Spacer(modifier = Modifier.width(8.dp))
        StatCard(RecipeStatus.DONE.displayName, done.toString(), Modifier.weight(1f), color = Color(0xFF4CAF50))
    }
}

@Composable
fun StatCard(title: String, count: String, modifier: Modifier = Modifier, color: Color = DarkBlue) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 14.sp, color = Color.Gray)
            Text(count, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun SearchBar(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Rechercher une recette...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = DarkBlue
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterActions(selectedStatus: RecipeStatus?, onStatusSelected: (RecipeStatus?) -> Unit, onAdd: () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = selectedStatus == null,
                onClick = { onStatusSelected(null) },
                label = { Text("Tous") }
            )
            RecipeStatus.entries.forEach { status ->
                FilterChip(
                    selected = selectedStatus == status,
                    onClick = { onStatusSelected(status) },
                    label = { Text(status.displayName) }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onAdd,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Ajouter")
            }
        }
    }
}

@Composable
fun EmptyState(onAdd: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Search, // Placeholder icon
            contentDescription = "Aucune recette",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFEDE7F6))
                .padding(24.dp),
            tint = Color(0xFF5E35B1)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("Aucune recette", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkBlue)
        Text(
            "Commencez par ajouter votre première recette !",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAdd,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
            Spacer(modifier = Modifier.width(4.dp))
            Text("Ajouter une recette")
        }
    }
}

@Composable
fun RecipeList(recipes: List<Recipe>, onRecipeClick: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(recipes) { recipe ->
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
}
