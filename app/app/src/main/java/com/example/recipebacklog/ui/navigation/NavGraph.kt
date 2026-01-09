package com.example.recipebacklog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipebacklog.data.auth.AuthRepository
import com.example.recipebacklog.ui.screens.about.AboutScreen
import com.example.recipebacklog.ui.screens.account.AccountScreen
import com.example.recipebacklog.ui.screens.addedit.AddEditRecipeScreen
import com.example.recipebacklog.ui.screens.home.HomeScreen
import com.example.recipebacklog.ui.screens.login.LoginScreen
import com.example.recipebacklog.ui.screens.register.RegisterScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(navController: NavHostController) {

    val authRepo = remember { AuthRepository() }
    val scope = rememberCoroutineScope()

    val startDestination =
        if (authRepo.currentUser != null) "home"
        else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("login") {
            LoginScreen(
                onLogin = { email, pass ->
                    scope.launch {
                        try {
                            authRepo.signIn(email, pass)
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } catch (e: Exception) {
                            // TODO afficher erreur (snackbar plus tard)
                            e.printStackTrace()
                        }
                    }
                },
                onRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegister = { email, pass ->
                    scope.launch {
                        try {
                            authRepo.signUp(email, pass)
                            navController.navigate("home") {
                                popUpTo("register") { inclusive = true }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("home") {
            HomeScreen(
                onAdd = { navController.navigate("addEditRecipe") },
                onRecipeClick = { id ->
                    navController.navigate("addEditRecipe/$id")
                },
                onAccount = { navController.navigate("account") },
                onAbout = { navController.navigate("about") }
            )
        }

        composable("account") {
            AccountScreen(
                userEmail = authRepo.currentUser?.email ?: "Unknown",
                onLogout = {
                    authRepo.signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("about") {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("addEditRecipe") {
            AddEditRecipeScreen(
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
