package com.example.recipebacklog.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel // _____Clément_____
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipebacklog.ui.home.HomeScreen
import com.example.recipebacklog.ui.home.HomeViewModel
import com.example.recipebacklog.ui.search.MealDetailScreen
import com.example.recipebacklog.ui.search.SearchScreen
import com.example.recipebacklog.ui.screens.login.LoginScreen
import com.example.recipebacklog.ui.screens.login.LoginViewModel // _____Clément_____
import com.example.recipebacklog.ui.screens.account.AccountScreen // _____Clément_____
import com.example.recipebacklog.ui.screens.about.AboutScreen // _____Clément_____
import com.example.recipebacklog.ui.screens.home.RecipeDetailScreen // _____Clément_____
import com.google.firebase.auth.FirebaseAuth // _____Clément_____

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()

    // Vérifier si un utilisateur est déjà connecté au lancement // _____Clément_____
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // LOGIN
        composable("login") {
            LaunchedEffect(loginViewModel.loginSuccess) {
                if (loginViewModel.loginSuccess) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }

            LoginScreen(
                onLogin = { email, password -> 
                    loginViewModel.login(email, password)
                },
                onRegister = { /* Navigation vers register si besoin */ }
            )
        }

        // HOME
        composable("home") {
            HomeScreen(
                viewModel = homeViewModel,
                onSearchClick = { navController.navigate("search") },
                onRecipeClick = { id -> navController.navigate("recipeDetail/$id") }, // _____Clément_____
                onAccountClick = { navController.navigate("account") }, // _____Clément_____
                onAboutClick = { navController.navigate("about") } // _____Clément_____
            )
        }

        // RECIPE DETAIL // _____Clément_____
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

        // ACCOUNT
        composable("account") {
            val user = FirebaseAuth.getInstance().currentUser
            AccountScreen(
                userEmail = user?.email ?: "Inconnu",
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    loginViewModel.resetLoginStatus()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() } // _____Clément_____
            )
        }

        // ABOUT
        composable("about") {
            AboutScreen(
                onBack = { navController.popBackStack() } // _____Clément_____
            )
        }

        // SEARCH
        composable("search") {
            SearchScreen(
                onMealClick = { mealId ->
                    navController.navigate("detail/$mealId")
                },
                onBack = { navController.popBackStack() } // _____Clément_____
            )
        }

        // DETAIL
        composable(
            route = "detail/{mealId}",
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
    }
}
