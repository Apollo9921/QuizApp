package com.example.quizapp.presentation.screens.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.presentation.components.BottomNavigationBar
import com.example.quizapp.presentation.components.ErrorScreen
import com.example.quizapp.presentation.core.Pink40
import com.example.quizapp.presentation.core.Purple40
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.White
import com.example.quizapp.presentation.navigation.Destination
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProgressRoute(
    navHostController: NavHostController,
    viewModel: ProgressViewModel = koinViewModel<ProgressViewModel>()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val fetchUser = { viewModel.fetchUser() }
    val navigateToLeaderboard = { navHostController.navigate(Destination.Leaderboard.route) }

    LaunchedEffect(Unit) {
        fetchUser()
    }

    Progress(
        uiState = uiState,
        fetchUser = fetchUser,
        navigateToLeaderboard = navigateToLeaderboard,
        navHostController = navHostController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Progress(
    uiState: ProgressViewModel.UIState,
    fetchUser: () -> Unit,
    navigateToLeaderboard: () -> Unit,
    navHostController: NavHostController
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.progress),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PurpleGrey40
                ),
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = { BottomNavigationBar(navHostController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            when (uiState) {
                is ProgressViewModel.UIState.Success -> {
                    val currentPoints = uiState.userData.currentPoints
                    val maxPoints = uiState.userData.maxPoints
                    val progress = uiState.userData.percentage.toFloat()

                    val animatedProgress = animateFloatAsState(
                        targetValue = progress,
                        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
                    ).value

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.almost_level_up),
                                style = MaterialTheme.typography.titleLarge,
                                color = White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.keep_answer_correctly),
                                style = MaterialTheme.typography.labelSmall,
                                color = White.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(250.dp)
                                .aspectRatio(1f)
                        ) {
                            CircularProgressIndicator(
                                progress = { 1f },
                                color = Pink40.copy(alpha = 0.2f),
                                strokeWidth = 14.dp,
                                strokeCap = StrokeCap.Round,
                                modifier = Modifier.fillMaxSize()
                            )

                            CircularProgressIndicator(
                                progress = { animatedProgress },
                                color = White,
                                strokeWidth = 14.dp,
                                strokeCap = StrokeCap.Round,
                                modifier = Modifier.fillMaxSize()
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${(progress * 100).toInt()}%",
                                    style = MaterialTheme.typography.displayMedium,
                                    color = White,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$currentPoints / $maxPoints XP",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = White.copy(alpha = 0.85f),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Button(
                            onClick = navigateToLeaderboard,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Leaderboard,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = stringResource(id = R.string.leaderboard),
                                style = MaterialTheme.typography.titleMedium,
                                color = White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                is ProgressViewModel.UIState.Error -> {
                    ErrorScreen(stringResource(uiState.errorMessage)) { fetchUser() }
                }

                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = White)
                    }
                }
            }
        }
    }
}