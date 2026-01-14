package com.example.recipebacklog.data.analytics

import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

object AnalyticsLogger {

    private val analytics: FirebaseAnalytics by lazy { Firebase.analytics}

    fun logLoginSuccess() {
        analytics.logEvent("login_success", null)
    }

    fun logRecipeImported(recipeId: String) {
        analytics.logEvent("recipe_imported", Bundle().apply {
            putString("recipe_id", recipeId)
        })
    }

    fun logRecipeSaved(recipeId: String) {
        analytics.logEvent("recipe_saved", Bundle().apply {
            putString("recipe_id", recipeId)
        })
    }

    fun logRecipeDeleted(recipeId: String) {
        analytics.logEvent("recipe_deleted", Bundle().apply {
            putString("recipe_id", recipeId)
        })
    }

    fun logRecipeViewed(recipeId: String) {
        analytics.logEvent("recipe_viewed", Bundle().apply {
            putString("recipe_id", recipeId)
        })
    }
}
