package com.example.recipebacklog

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class RecipeBacklogApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true) // ✅ active le cache offline
            .build()
    }
}
