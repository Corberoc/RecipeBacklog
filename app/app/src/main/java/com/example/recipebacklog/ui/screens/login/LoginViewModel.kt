package com.example.recipebacklog.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebacklog.data.auth.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var loginSuccess by mutableStateOf(false)

    fun login(email: String, password: String) { 
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Veuillez remplir tous les champs"
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                authRepository.signIn(email, password)
                loginSuccess = true 
            } catch (e: Exception) {
                errorMessage = "Erreur de connexion : ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun resetLoginStatus() { 
        loginSuccess = false
        errorMessage = null
    }
}
