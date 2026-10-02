package com.apollo9921.quizrise.presentation.screens.forgotPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.presentation.components.TopBar
import com.apollo9921.quizrise.presentation.core.QuizAppTheme
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen
import com.apollo9921.quizrise.R

@Composable
fun ForgotPasswordRoute(navHostController: NavHostController) {
    val navigateBack = { navHostController.navigateUp() }

    ForgotPasswordScreen(
        navigateBack = navigateBack
    )
}

@Composable
private fun ForgotPasswordScreen(
    navigateBack: () -> Boolean
) {
    var email by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopBar(
                backgroundColor = MaterialTheme.colorScheme.primary,
                onClick = { navigateBack() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                    modifier = Modifier.size(componentSizeByScreen(baseSize = 80.dp))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.LockReset,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(componentSizeByScreen(baseSize = 40.dp))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.forgot_password_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.surface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.forgot_password_description),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        //if (isEmailError) isEmailError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.email_field),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                        )
                    },
                    placeholder = {
                        Text(
                            text = "example@email.com",
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                        )
                    },
                    //isError = isEmailError,
                    supportingText = {
                        //if (isEmailError) {
                            Text(
                                text = stringResource(R.string.invalid_email_format),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                       //}
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            /*if (validateEmail()) {
                                onSendResetLinkClick(email.trim())
                            }*/
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.surface,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.surface,
                        unfocusedTextColor = MaterialTheme.colorScheme.surface,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        errorCursorColor = MaterialTheme.colorScheme.error,
                        errorLabelColor = MaterialTheme.colorScheme.error,
                        errorTrailingIconColor = MaterialTheme.colorScheme.error
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            /*if (validateEmail()) {
                                onSendResetLinkClick(email.trim())
                            }*/
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(componentSizeByScreen(baseSize = 56.dp))
                    ) {
                        Text(
                            text = stringResource(R.string.send_link),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    QuizAppTheme {
        ForgotPasswordScreen(navigateBack = { false })
    }
}