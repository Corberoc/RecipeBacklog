package com.example.recipebacklog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.example.recipebacklog.ui.screens.login.LoginScreen
import com.example.recipebacklog.ui.screens.register.RegisterScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLogin = { navController.navigate("home") },
                onRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegister = { navController.popBackStack() } // revient à Login ou navigue vers Home
            )
        }
        composable("home") { /* HomeScreen() */ }
        composable("addEditRecipe/{id?}") { /* AddEditRecipeScreen() */ }
        composable("about") { /* AboutScreen() */ }
        composable("account") { /* AccountScreen() */ }
    }
}
