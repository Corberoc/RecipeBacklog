/*package com.example.recipebacklog.data.recipes

import com.example.recipebacklog.model.Recipe
import com.example.recipebacklog.model.RecipeStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


import kotlinx.coroutines.tasks.await

class FirestoreRecipeRepository {

    private val db = FirebaseFirestore.getInstance()

}*/

package com.example.recipebacklog.data.recipes

import com.example.recipebacklog.model.Recipe
import com.example.recipebacklog.model.RecipeStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRecipeRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : RecipeRepository {

    private fun recipesCollection() =
        db.collection("users")
            .document(auth.currentUser?.uid ?: error("User not logged"))
            .collection("recipes")

    override suspend fun getAll(): List<Recipe> {
        val snap = recipesCollection().get().await()
        return snap.toObjects(Recipe::class.java)
    }

    override suspend fun upsert(recipe: Recipe) {
        // Si ton Recipe.id est déjà un String unique, on l’utilise comme docId
        recipesCollection()
            .document(recipe.id)
            .set(recipe)
            .await()
    }

    override suspend fun delete(recipeId: String) {
        recipesCollection()
            .document(recipeId)
            .delete()
            .await()
    }

    override suspend fun updateStatus(recipeId: String, status: RecipeStatus) {
        recipesCollection()
            .document(recipeId)
            .update("status", status)
            .await()
    }
}

