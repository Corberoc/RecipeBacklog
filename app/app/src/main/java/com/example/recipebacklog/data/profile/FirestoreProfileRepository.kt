package com.example.recipebacklog.data.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreProfileRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ProfileRepository {

    private fun doc() =
        db.collection("users")
            .document(auth.currentUser?.uid ?: error("User not logged"))
            .collection("profile")
            .document("me")

    override suspend fun getPhotoBase64(): String? {
        val snap = doc().get().await()
        return snap.getString("photoBase64")
    }

    override suspend fun setPhotoBase64(base64: String) {
        doc().set(mapOf("photoBase64" to base64)).await()
    }
}
