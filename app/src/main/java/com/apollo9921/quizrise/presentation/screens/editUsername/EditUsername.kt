package com.apollo9921.quizrise.presentation.screens.editUsername

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.Red
import com.apollo9921.quizrise.presentation.core.White
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen
import com.apollo9921.quizrise.presentation.utils.widthOfScreen
import org.koin.androidx.compose.koinViewModel
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.presentation.components.TopBar

@Composable
fun EditUserNameRoute(
    navHostController: NavHostController,
    viewModel: EditUsernameViewModel = koinViewModel<EditUsernameViewModel>()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    ChangeNameScreen(
        initialName = "",
        onBackClick = { navHostController.navigateUp() },
        onSaveClick = { newName ->
            viewModel.updateName(newName, navHostController)
        },
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangeNameScreen(
    initialName: String,
    onBackClick: () -> Boolean,
    onSaveClick: (String) -> Unit,
    uiState: EditUsernameViewModel.UiState
) {
    var userName by remember { mutableStateOf(initialName) }

    val screenWidth = widthOfScreen()
    val scrollState = rememberScrollState()

    val maxLayoutWidth = if (screenWidth < 600.dp) Dp.Unspecified else componentSizeByScreen(540.dp)

    Scaffold(
        topBar = {
            TopBar(
                backgroundColor = PurpleGrey40,
                isBackEnabled = true,
                onBackClicked = { onBackClick() }
            )
        },
        containerColor = PurpleGrey40
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = maxLayoutWidth)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(White.copy(alpha = 0.05f))
                            .border(2.dp, White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        val avatarUrl =
                            "https://api.dicebear.com/5.x/adventurer/png?seed=${userName.trim()}&backgroundColor=transparent"

                        AsyncImage(
                            model = avatarUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Text(
                        text = stringResource(R.string.avatar_update_by_typing),
                        style = MaterialTheme.typography.labelSmall,
                        color = White.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = {
                        Text(
                            text = stringResource(R.string.username),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = White.copy(alpha = 0.7f)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedBorderColor = White,
                        unfocusedBorderColor = White.copy(alpha = 0.4f),
                        focusedLabelColor = White,
                        unfocusedLabelColor = White.copy(alpha = 0.7f),
                        cursorColor = White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f, fill = false))

                if (uiState is EditUsernameViewModel.UiState.Error) {
                    Text(
                        text = stringResource(uiState.errorMessage),
                        style = MaterialTheme.typography.labelSmall,
                        color = Red.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = { onSaveClick(userName.trim()) },
                    enabled = userName.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = White,
                        contentColor = PurpleGrey40,
                        disabledContainerColor = White.copy(alpha = 0.3f),
                        disabledContentColor = PurpleGrey40.copy(alpha = 0.5f)
                    )
                ) {
                    if (uiState is EditUsernameViewModel.UiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = PurpleGrey40
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.save_changes),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewEditUserName() {
    ChangeNameScreen(
        initialName = "Jogador1",
        onBackClick = { false },
        onSaveClick = {},
        uiState = EditUsernameViewModel.UiState.Idle
    )
}