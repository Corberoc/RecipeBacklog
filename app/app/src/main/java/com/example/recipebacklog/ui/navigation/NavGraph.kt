package com.example.recipebacklog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.recipebacklog.data.auth.AuthRepository
import com.example.recipebacklog.ui.screens.about.AboutScreen
import com.example.recipebacklog.ui.screens.account.AccountScreen
import com.example.recipebacklog.ui.screens.addedit.AddEditRecipeScreen
import com.example.recipebacklog.ui.home.HomeScreen
import com.example.recipebacklog.ui.home.HomeViewModel
import com.example.recipebacklog.ui.screens.login.LoginScreen
import com.example.recipebacklog.ui.search.SearchScreen
import com.example.recipebacklog.ui.search.MealDetailScreen
import com.example.recipebacklog.ui.screens.home.RecipeDetailScreen // _____Clément_____
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.example.recipebacklog.data.analytics.AnalyticsLogger
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


@Composable
fun AppNavGraph(navController: NavHostController) {

    val authRepo = remember { AuthRepository() }
    val scope = rememberCoroutineScope()
    val homeViewModel: HomeViewModel = viewModel()

    val authState by authRepo.getAuthState().collectAsState(initial = authRepo.currentUser)

    val startDestination =
        if (authState != null) "home"
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
                            AnalyticsLogger.logLoginSuccess()
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
                onRegister = { email, pass ->
                    scope.launch {
                        try {
                            authRepo.signUp(email, pass)
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            )
        }

        composable("home") {
            androidx.compose.runtime.LaunchedEffect(Unit) {
                homeViewModel.loadRecipes()
            }

            HomeScreen(
                viewModel = homeViewModel,
                userName = authState?.displayName ?: "Utilisateur",
                onSearchClick = { navController.navigate("search") },
                onRecipeClick = { id -> navController.navigate("recipeDetail/$id") },
                onAccountClick = { navController.navigate("account") },
                onAboutClick = { navController.navigate("about") }
            )
        }


        // Nouvelle route pour le détail d'une recette enregistrée
        composable(
            "recipeDetail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
            val recipe = homeViewModel.recipes.find { it.id == recipeId }
            RecipeDetailScreen(
                recipe = recipe,
                onBack = { navController.popBackStack() }
            )
        }

        composable("account") {
            AccountScreen(
                onLogout = {
                    authRepo.signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() } // _____Clément_____
            )
        }

        composable("about") {
            AboutScreen(
                onBack = { navController.popBackStack() } // _____Clément_____
            )
        }

        composable("search") { // _____Clément_____
            SearchScreen(
                onMealClick = { id -> navController.navigate("detail/$id") },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "detail/{mealId}",
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
            MealDetailScreen(
                mealId = mealId,
                onImport = { recipe ->
                    homeViewModel.importRecipe(recipe)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() } // _____Clément_____
            )
        }
/*
        composable("addEditRecipe") {
            AddEditRecipeScreen(
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
 */
        composable("addEditRecipe") {
            AddEditRecipeScreen(
                onSave = { recipe ->
                    homeViewModel.addOrUpdateRecipe(recipe)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
