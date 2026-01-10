package com.example.recipebacklog.ui.screens.account

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebacklog.data.auth.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class PasswordChangeState {
    object Idle : PasswordChangeState()
    object Loading : PasswordChangeState()
    object Success : PasswordChangeState()
    data class Error(val message: String) : PasswordChangeState()
}

sealed class ProfileUpdateState {
    object Idle : ProfileUpdateState()
    object Loading : ProfileUpdateState()
    object Success : ProfileUpdateState()
    data class Error(val message: String) : ProfileUpdateState()
}

class AccountViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _passwordChangeState = MutableStateFlow<PasswordChangeState>(PasswordChangeState.Idle)
    val passwordChangeState = _passwordChangeState.asStateFlow()

    private val _profileUpdateState = MutableStateFlow<ProfileUpdateState>(ProfileUpdateState.Idle)
    val profileUpdateState = _profileUpdateState.asStateFlow()

    private val _user = MutableStateFlow(authRepository.currentUser)
    val user = _user.asStateFlow()

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _passwordChangeState.value = PasswordChangeState.Loading
            val result = authRepository.reauthenticateAndChangePassword(currentPassword, newPassword)
            _passwordChangeState.value = if (result.isSuccess) {
                PasswordChangeState.Success
            } else {
                PasswordChangeState.Error(result.exceptionOrNull()?.message ?: "An unknown error occurred")
            }
        }
    }

    fun resetPasswordChangeState() {
        _passwordChangeState.value = PasswordChangeState.Idle
    }

    fun updateProfile(displayName: String, photoUri: Uri?) {
        viewModelScope.launch {
            _profileUpdateState.value = ProfileUpdateState.Loading
            val result = authRepository.updateProfile(displayName, photoUri)
            if (result.isSuccess) {
                try {
                    authRepository.currentUser?.reload()?.await()
                    _user.value = authRepository.currentUser
                    _profileUpdateState.value = ProfileUpdateState.Success
                } catch (e: Exception) {
                    _profileUpdateState.value = ProfileUpdateState.Error(e.message ?: "Failed to refresh user.")
                }
            } else {
                _profileUpdateState.value = ProfileUpdateState.Error(result.exceptionOrNull()?.message ?: "An unknown error occurred")
            }
        }
    }

    fun resetProfileUpdateState() {
        _profileUpdateState.value = ProfileUpdateState.Idle
    }
}