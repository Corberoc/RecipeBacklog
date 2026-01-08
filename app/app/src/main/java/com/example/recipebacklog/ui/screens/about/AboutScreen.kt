package com.example.recipebacklog.ui.screens.about

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit // callback pour revenir à HomeScreen
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("About") })
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
                text = "This app is a demo project to have a very good grade.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onBack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}
