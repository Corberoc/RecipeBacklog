package com.example.recipebacklog.data.auth

import android.net.Uri
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    val currentUser get() = auth.currentUser

    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email.trim(), password).await()
    }

    suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email.trim(), password).await()
    }

    fun signOut() = auth.signOut()

    suspend fun reauthenticateAndChangePassword(currentPassword: String, newPassword: String): Result<Unit> {
        return try {
            val user = auth.currentUser ?: throw IllegalStateException("User not logged in")
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential).await()
            user.updatePassword(newPassword).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(displayName: String, photoUri: Uri?): Result<Unit> {
        return try {
            val user = auth.currentUser ?: throw IllegalStateException("User not logged in")

            // If a new photo is provided, upload it and get the new URL.
            // Otherwise, we keep the existing URL.
            val photoDownloadUrl = if (photoUri != null) {
                val photoRef = storage.reference.child("profile_pictures/${user.uid}")
                photoRef.putFile(photoUri).await()
                photoRef.downloadUrl.await()
            } else {
                user.photoUrl
            }

            // Build the request with the new display name and the definitive photo URL (new or old).
            val profileUpdates = userProfileChangeRequest {
                this.displayName = displayName
                this.photoUri = photoDownloadUrl
            }

            // Apply the updates.
            user.updateProfile(profileUpdates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}