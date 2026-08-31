package com.apollo9921.quizrise.presentation.screens.deleteAccount

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.presentation.components.TopBar
import com.apollo9921.quizrise.presentation.core.Black
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.White
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.presentation.components.ErrorScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeleteAccountRoute(
    navHostController: NavHostController,
    viewModel: DeleteAccountViewModel = koinViewModel<DeleteAccountViewModel>()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val navigateBack = { navHostController.navigateUp() }
    val onDeleteConfirmed = { viewModel.deleteAccount(navHostController) }

    DeleteAccountScreen(
        state = state,
        onCancelClick = navigateBack,
        onDeleteConfirmClick = onDeleteConfirmed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteAccountScreen(
    state: DeleteAccountViewModel.UIState,
    onCancelClick: () -> Boolean,
    onDeleteConfirmClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                backgroundColor = PurpleGrey40,
                onClick = { onCancelClick() }
            )
        },
        containerColor = PurpleGrey40
    ) { paddingValues ->
        when (state) {
            is DeleteAccountViewModel.UIState.Error -> {
                ErrorScreen(
                    errorMessage = stringResource(state.message),
                    onClick = { onDeleteConfirmClick() }
                )
            }
            DeleteAccountViewModel.UIState.Idle -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, White.copy(alpha = 0.15f)),
                            colors = CardDefaults.cardColors(
                                containerColor = White.copy(alpha = 0.06f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(id = R.string.important_warning).uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFF87171),
                                    fontWeight = FontWeight.Black,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = stringResource(id = R.string.warning_message),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(24.dp))
                                HorizontalDivider(color = White.copy(alpha = 0.1f))
                                Spacer(modifier = Modifier.height(20.dp))

                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    BulletWarningItem(text = stringResource(id = R.string.all_points_warning))
                                    BulletWarningItem(text = stringResource(id = R.string.progress_warning))
                                    BulletWarningItem(text = stringResource(id = R.string.history_warning))
                                    BulletWarningItem(text = stringResource(id = R.string.leaderboard_warning))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(componentSizeByScreen(baseSize = 40.dp)))

                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(componentSizeByScreen(baseSize = 54.dp))
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { onCancelClick() }
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.keep_account),
                                    color = Black,
                                    style = MaterialTheme.typography.labelMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(componentSizeByScreen(baseSize = 50.dp))
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { onDeleteConfirmClick() },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (state is DeleteAccountViewModel.UIState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.delete_account),
                                    color = Color(0xFFF87171),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun BulletWarningItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "•",
            color = Color(0xFFF87171),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = text,
            color = White.copy(alpha = 0.7f),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteAccountScreenPreview() {
    MaterialTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)) {
            DeleteAccountScreen(
                onCancelClick = { false },
                onDeleteConfirmClick = {},
                state = DeleteAccountViewModel.UIState.Idle
            )
        }
    }
}