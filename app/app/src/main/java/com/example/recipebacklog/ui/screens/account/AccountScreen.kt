package com.example.recipebacklog.ui.screens.account

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.recipebacklog.R
import com.example.recipebacklog.ui.theme.DarkBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onLogout: () -> Unit,
    onBack: () -> Unit,
    accountViewModel: AccountViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val user by accountViewModel.user.collectAsState()
    val displayUri by accountViewModel.displayUri.collectAsState()
    val profileUpdateState by accountViewModel.profileUpdateState.collectAsState()
    val passwordChangeState by accountViewModel.passwordChangeState.collectAsState()

    var displayName by remember(user?.displayName) { mutableStateOf(user?.displayName ?: "") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { accountViewModel.onPhotoSelected(context, it) }
    }

    LaunchedEffect(user) {
        user?.let { accountViewModel.initialize(context) }
    }

    LaunchedEffect(profileUpdateState) {
        when (val state = profileUpdateState) {
            is ProfileUpdateState.Success -> {
                snackbarHostState.showSnackbar("Profil mis à jour avec succès!")
                accountViewModel.resetProfileUpdateState()
            }
            is ProfileUpdateState.Error -> {
                snackbarHostState.showSnackbar("Erreur: ${state.message}")
                accountViewModel.resetProfileUpdateState()
            }
            else -> { /* Idle or Loading */ }
        }
    }

    LaunchedEffect(passwordChangeState) {
        when (val state = passwordChangeState) {
            is PasswordChangeState.Success -> {
                snackbarHostState.showSnackbar("Mot de passe changé avec succès!")
                accountViewModel.resetPasswordChangeState()
                currentPassword = ""
                newPassword = ""
                confirmNewPassword = ""
            }
            is PasswordChangeState.Error -> {
                snackbarHostState.showSnackbar("Erreur: ${state.message}")
                accountViewModel.resetPasswordChangeState()
            }
            else -> { /* Idle or Loading */ }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mon Compte") },
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = displayUri,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.etchebest),
                error = painterResource(id = R.drawable.etchebest)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Section
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Pseudo") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = user?.email ?: "",
                    onValueChange = { /* Email is not editable */ },
                    label = { Text("Email") },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("L'email ne peut pas être modifié", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { accountViewModel.updateProfile(displayName) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = profileUpdateState !is ProfileUpdateState.Loading,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
                ) {
                    if (profileUpdateState is ProfileUpdateState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Enregistrer les modifications")
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 24.dp))

            // Change Password Section
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text("Changer le mot de passe", style = MaterialTheme.typography.titleLarge)
                Text("Mettez à jour votre mot de passe", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Mot de passe actuel") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Nouveau mot de passe") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmNewPassword,
                    onValueChange = { confirmNewPassword = it },
                    label = { Text("Confirmer le nouveau mot de passe") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { accountViewModel.changePassword(currentPassword, newPassword) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = passwordChangeState !is PasswordChangeState.Loading,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
                ) {
                    if (passwordChangeState is PasswordChangeState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Changer le mot de passe")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
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
