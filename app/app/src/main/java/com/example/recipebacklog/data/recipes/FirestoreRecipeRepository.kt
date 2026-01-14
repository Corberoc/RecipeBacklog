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

        return snap.documents.mapNotNull { doc ->
            val title = doc.getString("title") ?: return@mapNotNull null
            val description = doc.getString("description")
            val imageUrl = doc.getString("imageUrl")

            val statusStr = doc.getString("status") ?: RecipeStatus.BACKLOG.name
            val status = runCatching { RecipeStatus.valueOf(statusStr) }
                .getOrDefault(RecipeStatus.BACKLOG)

            val isFavorite = doc.getBoolean("isFavorite")
                ?: doc.getBoolean("favorite")  // fallback si tu changes plus tard
                ?: false

            Recipe(
                id = doc.id,
                title = title,
                description = description,
                imageUrl = imageUrl,
                status = status,
                isFavorite = isFavorite
            )
        }
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

    override suspend fun updateFavorite(recipeId: String, isFavorite: Boolean) {
        recipesCollection()
            .document(recipeId)
            .update("isFavorite", isFavorite)
            .await()
    }
}

