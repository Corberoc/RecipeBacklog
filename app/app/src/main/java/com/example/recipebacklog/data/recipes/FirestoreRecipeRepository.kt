package com.example.recipebacklog.data.recipes

import com.example.recipebacklog.domain.models.Recipe
import com.example.recipebacklog.domain.models.RecipeStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


import kotlinx.coroutines.tasks.await

class FirestoreRecipeRepository {

    private val db = FirebaseFirestore.getInstance()

}
