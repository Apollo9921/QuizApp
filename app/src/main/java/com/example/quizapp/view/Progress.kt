package com.example.quizapp.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.view.bottomBar.BottomNavigationBar
import com.example.quizapp.view.custom.*
import com.example.quizapp.view.theme.PurpleGrey40
import com.example.quizapp.view.theme.White
import com.example.quizapp.viewModel.ProgressViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProgressRoute(
    navHostController: NavHostController,
    viewModel: ProgressViewModel = koinViewModel<ProgressViewModel>()
) {
    val uiState = viewModel.uiState.collectAsState().value
    Progress(
        uiState = uiState,
        navHostController = navHostController
    )
}

@Composable
private fun Progress(uiState: ProgressViewModel.UIState, navHostController: NavHostController) {
    Scaffold(
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
                    val progress = remember { mutableStateOf(uiState.userData.percentage.toFloat()) }
                    val animatedProgress = animateFloatAsState(
                        targetValue = progress.value,
                        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
                    ).value
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.progress),
                            color = White,
                            fontSize =
                                if (mediaQueryWidth() <= small) {
                                    35.sp
                                } else if (mediaQueryWidth() <= normal) {
                                    40.sp
                                } else {
                                    45.sp
                                },
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box {
                            Text(
                                text = "${totalPoints}/${badge}",
                                color = White,
                                fontSize =
                                    if (mediaQueryWidth() <= small) {
                                        25.sp
                                    } else if (mediaQueryWidth() <= normal) {
                                        35.sp
                                    } else {
                                        45.sp
                                    },
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .offset(
                                        y =
                                            if (mediaQueryWidth() <= small) {
                                                115.dp
                                            } else if (mediaQueryWidth() <= normal) {
                                                175.dp
                                            } else {
                                                235.dp
                                            },
                                        x =
                                            if (mediaQueryWidth() <= small) {
                                                70.dp
                                            } else if (mediaQueryWidth() <= normal) {
                                                130.dp
                                            } else {
                                                160.dp
                                            }
                                    )
                            )
                            CircularProgressIndicator(
                                progress = animatedProgress,
                                color = White,
                                strokeWidth =
                                    if (mediaQueryWidth() <= small) {
                                        15.dp
                                    } else if (mediaQueryWidth() <= normal) {
                                        20.dp
                                    } else {
                                        25.dp
                                    },
                                modifier = Modifier
                                    .size(
                                        if (mediaQueryWidth() <= small) {
                                            250.dp
                                        } else if (mediaQueryWidth() <= normal) {
                                            350.dp
                                        } else {
                                            450.dp
                                        }
                                    )
                            )
                        }
                    }
                }

                else -> {}
            }
        }
    }
}