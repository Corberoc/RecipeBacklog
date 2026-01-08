package com.example.recipebacklog.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun RegisterScreen(
    onRegister: () -> Unit,   // callback pour revenir ou naviguer vers Login/Home
) {
    // États pour les champs
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Layout principal
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Register",
                style = MaterialTheme.typography.headlineMedium
            )

            // Champ email
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Champ password
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Champ confirmation
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Bouton register
            Button(
                onClick = {
                    println("REGISTER MOCK: $email")
                    onRegister()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }
        }
    }
}
