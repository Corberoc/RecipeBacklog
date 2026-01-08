package com.example.recipebacklog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.example.recipebacklog.ui.screens.about.AboutScreen
import com.example.recipebacklog.ui.screens.account.AccountScreen
import com.example.recipebacklog.ui.screens.addedit.AddEditRecipeScreen
import com.example.recipebacklog.ui.screens.home.HomeScreen
import com.example.recipebacklog.ui.screens.home.mockRecipes
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
        composable("home") {
            HomeScreen(
                onAdd = { navController.navigate("addEditRecipe") },
                onRecipeClick = { id -> navController.navigate("addEditRecipe/$id") },
                onAccount = { navController.navigate("account") },
                onAbout = { navController.navigate("about") }
            )
        }
        composable("addEditRecipe") {
            AddEditRecipeScreen(
                onSave = { recipe ->
                    println("SAVE MOCK: ${recipe.title}")
                    navController.popBackStack() // retourne à Home
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("addEditRecipe/{id}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("id")
            val recipe = mockRecipes.find { it.id == recipeId }
            AddEditRecipeScreen(
                recipe = recipe,
                onSave = { updated ->
                    println("UPDATE MOCK: ${updated.title}")
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("about") {
            AboutScreen(
                onBack = { navController.popBackStack() } // revient à HomeScreen
            )
        }
        composable("account") {
            AccountScreen(
                onLogout = { navController.navigate("login") { popUpTo("home") { inclusive = true } } }
            )
        }
    }
}
