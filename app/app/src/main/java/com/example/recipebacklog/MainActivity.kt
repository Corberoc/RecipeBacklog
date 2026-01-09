package com.example.recipebacklog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.recipebacklog.data.auth.AuthRepository
import kotlinx.coroutines.launch
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent { MaterialTheme { App() } }
    }
}

@Composable
private fun App() {
    val repo = remember { AuthRepository() }
    val nav = rememberNavController()
    val start = if (repo.currentUser != null) "home" else "login"

    NavHost(navController = nav, startDestination = start) {
        composable("login") {
            AuthScreen(
                title = "Login",
                mainButton = "Se connecter",
                secondaryButton = "Créer un compte",
                onMain = { email, pass ->
                    repo.signIn(email, pass)
                    nav.navigate("home") { popUpTo("login") { inclusive = true } }
                },
                onSecondary = { nav.navigate("register") }
            )
        }
        composable("register") {
            AuthScreen(
                title = "Register",
                mainButton = "Créer le compte",
                secondaryButton = "Retour login",
                onMain = { email, pass ->
                    repo.signUp(email, pass)
                    nav.navigate("home") { popUpTo("register") { inclusive = true } }
                },
                onSecondary = { nav.popBackStack() }
            )
        }
        composable("home") {
            HomeScreen(
                email = repo.currentUser?.email ?: "Unknown",
                onLogout = {
                    repo.signOut()
                    nav.navigate("login") { popUpTo("home") { inclusive = true } }
                }
            )
        }
    }
}

@Composable
private fun AuthScreen(
    title: String,
    mainButton: String,
    secondaryButton: String,
    onMain: suspend (String, String) -> Unit,
    onSecondary: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Password (6+)") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(error!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                error = null
                loading = true
                scope.launch {
                    try {
                        onMain(email, password)
                    } catch (e: Exception) {
                        error = e.message ?: "Erreur"
                    } finally {
                        loading = false
                    }
                }
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (loading) "..." else mainButton)
        }

        TextButton(onClick = onSecondary, modifier = Modifier.fillMaxWidth()) {
            Text(secondaryButton)
        }
    }
}

@Composable
private fun HomeScreen(email: String, onLogout: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Home", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Connecté : $email")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Logout")
        }
    }
}
