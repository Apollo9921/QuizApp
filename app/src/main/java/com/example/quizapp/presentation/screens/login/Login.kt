package com.example.quizapp.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.White
import com.example.quizapp.presentation.navigation.Destination
import com.google.android.gms.common.SignInButton
import org.koin.androidx.compose.koinViewModel
import com.example.quizapp.R

@Composable
fun LoginRoute(
    navHostController: NavHostController,
    viewModel: LoginViewModel = koinViewModel<LoginViewModel>()
) {
    val state = viewModel.uiState.collectAsState().value
    val onLoginClick =
        { email: String, password: String ->
            viewModel.loginWithEmail(
                email,
                password,
                navHostController
            )
        }
    val onGoogleSignInClick = { viewModel.startSignInByGoogle(navHostController) }
    val navigateToRegister = { navHostController.navigate(Destination.Register.route) }

    LoginScreen(
        state = state,
        onLoginClick = onLoginClick,
        onGoogleSignInClick = onGoogleSignInClick,
        navigateToRegister = navigateToRegister
    )
}

@Composable
private fun LoginScreen(
    state: LoginViewModel.UIState,
    onLoginClick: (String, String) -> Unit,
    onGoogleSignInClick: () -> Unit,
    navigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.greeting),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.login_to_account),
            style = MaterialTheme.typography.labelMedium,
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

        if (state is LoginViewModel.UIState.Error) {
            Text(
                text = stringResource(state.message),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = { onLoginClick(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = state !is LoginViewModel.UIState.Loading
        ) {
            if (state is LoginViewModel.UIState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    style = MaterialTheme.typography.labelMedium,
                    text = stringResource(R.string.login_btn),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.another_option),
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = White
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        AndroidView(
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = 16.dp),
            factory = { context ->
                SignInButton(context).apply {
                    setSize(SignInButton.SIZE_WIDE)
                    setColorScheme(SignInButton.COLOR_LIGHT)
                    setOnClickListener {
                        onGoogleSignInClick()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navigateToRegister() }) {
            Text(
                style = MaterialTheme.typography.labelSmall,
                text = stringResource(R.string.do_not_have_account),
                color = White
            )
        }
    }
}