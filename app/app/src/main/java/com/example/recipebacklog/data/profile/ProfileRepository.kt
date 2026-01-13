package com.example.recipebacklog.data.profile

interface ProfileRepository {
    suspend fun getPhotoBase64(): String?
    suspend fun setPhotoBase64(base64: String)
}
