package com.apollo9921.quizrise.presentation.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.White
import org.koin.androidx.compose.koinViewModel
import com.apollo9921.quizrise.R

@Composable
fun RegisterRoute(
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)
            .safeDrawingPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 500.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = stringResource(R.string.create_account),
                color = White
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        style = MaterialTheme.typography.labelSmall,
                        text = stringResource(R.string.email_field),
                        color = White
                    )
                },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = White) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = White,
                    unfocusedBorderColor = White.copy(alpha = 0.7f),
                    cursorColor = White,
                    focusedTextColor = White,
                    unfocusedTextColor = White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        style = MaterialTheme.typography.labelSmall,
                        text = stringResource(R.string.password_field),
                        color = White
                    )
                },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = White) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = White
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = White,
                    unfocusedBorderColor = White.copy(alpha = 0.7f),
                    cursorColor = White,
                    focusedTextColor = White,
                    unfocusedTextColor = White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = {
                    Text(
                        style = MaterialTheme.typography.labelSmall,
                        text = stringResource(R.string.confirm_password_field),
                        color = White
                    )
                },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = White) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = White,
                    unfocusedBorderColor = White.copy(alpha = 0.7f),
                    cursorColor = White,
                    focusedTextColor = White,
                    unfocusedTextColor = White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (state is RegisterViewModel.UIState.Error) {
                Text(
                    text = stringResource(state.message),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = {
                    viewModel.onRegisterClick(
                        email,
                        password,
                        confirmPassword,
                        onNavigateBack
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = state !is RegisterViewModel.UIState.Loading
            ) {
                if (state is RegisterViewModel.UIState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        style = MaterialTheme.typography.labelMedium,
                        text = stringResource(R.string.register),
                        color = White
                    )
                }
            }

            TextButton(onClick = onNavigateBack) {
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    text = stringResource(R.string.have_an_account),
                    color = White
                )
            }
        }
    }
}