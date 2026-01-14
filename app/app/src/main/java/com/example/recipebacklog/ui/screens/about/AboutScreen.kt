package com.example.recipebacklog.ui.screens.about

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons // _____Clément_____
import androidx.compose.material.icons.filled.ArrowBack // _____Clément_____
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Recipe Backlog App",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Version: 1.0.0",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Developed by LES SAUCIFLARD(S)",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Cette application est un projet d'étude pour le cour d'android",
                style = MaterialTheme.typography.bodyMedium
            )
            Button(
                onClick = { throw RuntimeException("Test Crashlytics") }
            ) {
                Text("Test Crashlytics")
            }
        }
    }
}
