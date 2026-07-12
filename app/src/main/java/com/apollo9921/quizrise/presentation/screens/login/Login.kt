package com.apollo9921.quizrise.presentation.screens.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.White
import com.apollo9921.quizrise.presentation.navigation.Destination
import com.google.android.gms.common.SignInButton
import org.koin.androidx.compose.koinViewModel
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.data.repository.GoogleSignInLauncherHolder
import org.koin.compose.koinInject

@Composable
fun LoginRoute(
    navHostController: NavHostController,
    viewModel: LoginViewModel = koinViewModel<LoginViewModel>()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    val launcherHolder: GoogleSignInLauncherHolder = koinInject()
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        launcherHolder.handleResult(result.resultCode, result.data)
    }

    DisposableEffect(googleSignInLauncher) {
        launcherHolder.registerLauncher(googleSignInLauncher)
        onDispose {
            launcherHolder.clearLauncher()
        }
    }

    val onLoginClick =
        remember {
            { email: String, password: String ->
                viewModel.loginWithEmail(
                    email,
                    password,
                    navHostController
                )
            }
        }
    val onGoogleSignInClick = remember { { viewModel.startSignInByGoogle(navHostController) } }
    val navigateToRegister = remember { { navHostController.navigate(Destination.Register.route) } }

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
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.greeting),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = White
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(R.string.login_to_account),
            style = MaterialTheme.typography.labelMedium,
            color = White
        )

        Spacer(modifier = Modifier.padding(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    style = MaterialTheme.typography.displaySmall,
                    text = stringResource(R.string.email_field),
                    color = White
                )
            },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = White) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = White,
                unfocusedBorderColor = White,
                cursorColor = White,
                focusedTextColor = White,
                unfocusedTextColor = White
            )
        )

        Spacer(modifier = Modifier.padding(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    style = MaterialTheme.typography.displaySmall,
                    text = stringResource(R.string.password_field),
                    color = White
                )
            },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = White) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = White
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = White,
                unfocusedBorderColor = White,
                cursorColor = White,
                focusedTextColor = White,
                unfocusedTextColor = White
            )
        )

        Spacer(modifier = Modifier.padding(24.dp))

        if (state is LoginViewModel.UIState.Error) {
            Text(
                style = MaterialTheme.typography.displaySmall,
                text = stringResource(state.message),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = { onLoginClick(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
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
                    style = MaterialTheme.typography.labelSmall,
                    text = stringResource(R.string.login_btn),
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
                style = MaterialTheme.typography.labelSmall,
                color = White
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        AndroidView(
            modifier = Modifier
                .wrapContentSize()
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

        Spacer(modifier = Modifier.padding(16.dp))

        TextButton(onClick = { navigateToRegister() }) {
            Text(
                style = MaterialTheme.typography.displaySmall,
                text = stringResource(R.string.do_not_have_account),
                color = White
            )
        }
    }
}