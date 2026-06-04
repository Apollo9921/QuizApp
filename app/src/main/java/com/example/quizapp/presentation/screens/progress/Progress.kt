package com.example.quizapp.presentation.screens.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.presentation.components.BottomNavigationBar
import com.example.quizapp.presentation.components.ErrorScreen
import com.example.quizapp.presentation.core.Pink40
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.White
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProgressRoute(
    navHostController: NavHostController,
    viewModel: ProgressViewModel = koinViewModel<ProgressViewModel>()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val fetchUser = { viewModel.fetchUser() }
    LaunchedEffect(Unit) {
        fetchUser()
    }
    Progress(
        uiState = uiState,
        fetchUser = fetchUser,
        navHostController = navHostController
    )
}

@Composable
private fun Progress(
    uiState: ProgressViewModel.UIState,
    fetchUser: () -> Unit,
    navHostController: NavHostController
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PurpleGrey40)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(id = R.string.progress),
                    color = White
                )
            }
        },
        bottomBar = { BottomNavigationBar(navHostController) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .padding(bottom = it.calculateBottomPadding())
        ) {
            when (uiState) {
                is ProgressViewModel.UIState.Success -> {
                    val totalPoints = uiState.userData.totalPoints
                    val badge = uiState.userData.badge
                    val progress = uiState.userData.percentage.toFloat()
                    val animatedProgress = animateFloatAsState(
                        targetValue = progress,
                        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
                    ).value

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                style = MaterialTheme.typography.titleLarge,
                                text = "${totalPoints}/${badge}",
                                color = White
                            )
                            CircularProgressIndicator(
                                progress = animatedProgress,
                                color = White,
                                trackColor = Pink40,
                                strokeWidth = 20.dp,
                                modifier = Modifier
                                    .fillMaxSize(0.7f)
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }

                is ProgressViewModel.UIState.Error -> {
                    ErrorScreen(stringResource(uiState.errorMessage)) { fetchUser() }
                }

                else -> {}
            }
        }
    }
}