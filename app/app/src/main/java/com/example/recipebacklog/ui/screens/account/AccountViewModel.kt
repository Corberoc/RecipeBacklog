package com.example.recipebacklog.ui.screens.account

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebacklog.data.auth.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

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
    private val authRepository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _passwordChangeState = MutableStateFlow<PasswordChangeState>(PasswordChangeState.Idle)
    val passwordChangeState = _passwordChangeState.asStateFlow()

    private val _profileUpdateState = MutableStateFlow<ProfileUpdateState>(ProfileUpdateState.Idle)
    val profileUpdateState = _profileUpdateState.asStateFlow()

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user = _user.asStateFlow()

    private val _displayUri = MutableStateFlow<Uri?>(null)
    val displayUri = _displayUri.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getAuthState().collect { user ->
                _user.value = user
            }
        }
    }

    fun initialize(context: Context) {
        viewModelScope.launch {
            _user.value?.uid?.let { userId ->
                val profilePictureFile = File(context.filesDir, "profile_picture_$userId.jpg")
                if (profilePictureFile.exists()) {
                    val uri = FileProvider.getUriForFile(
                        context,
                        "com.example.recipebacklog.provider",
                        profilePictureFile
                    )
                    _displayUri.value = uri.buildUpon()
                        .appendQueryParameter("v", profilePictureFile.lastModified().toString())
                        .build()
                }
            }
        }
    }

    fun onPhotoSelected(context: Context, contentUri: Uri) {
        viewModelScope.launch {
            _user.value?.uid?.let { userId ->
                val localFileUri = withContext(Dispatchers.IO) {
                    copyUriToInternalStorage(context, contentUri, userId)
                }

                if (localFileUri != null) {
                    val profilePictureFile = File(context.filesDir, "profile_picture_$userId.jpg")
                    _displayUri.value = localFileUri.buildUpon()
                        .appendQueryParameter("v", profilePictureFile.lastModified().toString())
                        .build()
                } else {
                    // Optionally handle the error, e.g., show a snackbar
                }
            }
        }
    }

    private fun copyUriToInternalStorage(context: Context, contentUri: Uri, userId: String): Uri? {
        return try {
            val outputFile = File(context.filesDir, "profile_picture_$userId.jpg")
            context.contentResolver.openInputStream(contentUri)?.use { inputStream ->
                FileOutputStream(outputFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            FileProvider.getUriForFile(context, "com.example.recipebacklog.provider", outputFile)
        } catch (e: Exception) {
            null
        }
    }

    fun updateProfile(displayName: String) {
        viewModelScope.launch {
            _profileUpdateState.value = ProfileUpdateState.Loading
            val result = authRepository.updateProfile(displayName)
            if (result.isSuccess) {
                _profileUpdateState.value = ProfileUpdateState.Success
            } else {
                _profileUpdateState.value = ProfileUpdateState.Error(result.exceptionOrNull()?.message ?: "An unknown error occurred")
            }
        }
    }

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

    fun resetProfileUpdateState() {
        _profileUpdateState.value = ProfileUpdateState.Idle
    }
}
