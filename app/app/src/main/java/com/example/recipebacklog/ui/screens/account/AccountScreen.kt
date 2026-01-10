package com.example.recipebacklog.ui.screens.account

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.recipebacklog.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onLogout: () -> Unit,
    onBack: () -> Unit,
    accountViewModel: AccountViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val user by accountViewModel.user.collectAsState()
    var displayName by remember(user?.displayName) { mutableStateOf(user?.displayName ?: "") }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    val passwordChangeState by accountViewModel.passwordChangeState.collectAsState()
    val profileUpdateState by accountViewModel.profileUpdateState.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedPhotoUri = uri
    }

    LaunchedEffect(passwordChangeState) {
        when (val state = passwordChangeState) {
            is PasswordChangeState.Success -> {
                snackbarHostState.showSnackbar("Password changed successfully!")
                currentPassword = ""
                newPassword = ""
                accountViewModel.resetPasswordChangeState()
            }
            is PasswordChangeState.Error -> snackbarHostState.showSnackbar(state.message)
            else -> {}
        }
    }

    LaunchedEffect(profileUpdateState) {
        when (val state = profileUpdateState) {
            is ProfileUpdateState.Success -> {
                snackbarHostState.showSnackbar("Profile updated successfully!")
                displayName = user?.displayName ?: ""
                selectedPhotoUri = null
                accountViewModel.resetProfileUpdateState()
            }
            is ProfileUpdateState.Error -> snackbarHostState.showSnackbar(state.message)
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Make the column scrollable
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Profile Picture
            AsyncImage(
                model = selectedPhotoUri ?: user?.photoUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.etchebest),
                error = painterResource(id = R.drawable.etchebest)
            )

            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Display Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { accountViewModel.updateProfile(displayName, selectedPhotoUri) },
                enabled = profileUpdateState != ProfileUpdateState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (profileUpdateState == ProfileUpdateState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Update Profile")
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Change Password", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = { Text("Current Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { accountViewModel.changePassword(currentPassword, newPassword) },
                enabled = passwordChangeState != PasswordChangeState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (passwordChangeState == PasswordChangeState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Update Password")
                }
            }

            Spacer(modifier = Modifier.weight(1f, fill = false))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Logout")
            }
        }
    }
}