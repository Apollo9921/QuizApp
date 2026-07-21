package com.apollo9921.quizrise.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.White
import com.google.android.gms.common.SignInButton
import com.apollo9921.quizrise.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestLimitBottomSheet(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    onEmailRegisterClick: () -> Unit
) {
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            containerColor = PurpleGrey40,
            scrimColor = Color.Black.copy(alpha = 0.6f),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            dragHandle = {
                BottomSheetDefaults.DragHandle(color = White.copy(alpha = 0.4f))
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp, top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Surface(
                    shape = RoundedCornerShape(50),
                    color = White.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, White.copy(alpha = 0.2f)),
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.anonymous_quiz_expired),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.anonymous_quiz_expired_description),
                    style = MaterialTheme.typography.labelMedium,
                    color = White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

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

                Button(
                    onClick = { onEmailRegisterClick() },
                    modifier = Modifier
                        .wrapContentSize(),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, White.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = stringResource(R.string.create_account_email),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    )
                }
            }
        }
    }
}