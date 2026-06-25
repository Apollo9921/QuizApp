package com.apollo9921.quizrise.presentation.screens.boarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.apollo9921.quizrise.presentation.isSplashScreenOpen
import com.apollo9921.quizrise.presentation.navigation.Destination
import com.apollo9921.quizrise.presentation.core.Purple40
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.White
import kotlinx.coroutines.*
import android.content.res.Configuration
import androidx.compose.runtime.saveable.rememberSaveable
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.presentation.dataStore.UserManager
import com.apollo9921.quizrise.presentation.dataStore.dataStoreUser
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen

@Composable
fun OnBoard(navHostController: NavHostController) {
    LaunchedEffect(Unit) {
        isSplashScreenOpen = false
    }
    val pageCount = 5
    val state = rememberPagerState(pageCount = { pageCount })
    val coroutineScope = rememberCoroutineScope()

    val onBoardingTitle = rememberSaveable {
        listOf(
            R.string.onBoard1,
            R.string.onBoard2,
            R.string.onBoard3,
            R.string.onBoard4,
            R.string.onBoard5,
        )
    }

    val onBoardingAnimation = rememberSaveable {
        listOf(
            R.raw.welcome,
            R.raw.questions,
            R.raw.results,
            R.raw.badges,
            R.raw.start,
        )
    }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = if (isLandscape) 16.dp else 40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (state.currentPage > 0) {
                    Text(
                        style = MaterialTheme.typography.labelMedium,
                        text = stringResource(id = R.string.back),
                        color = White,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                coroutineScope.launch { state.animateScrollToPage(state.currentPage - 1) }
                            }
                    )
                } else {
                    Spacer(modifier = Modifier.size(1.dp))
                }
                if (state.currentPage < pageCount - 1) {
                    Text(
                        style = MaterialTheme.typography.labelMedium,
                        text = stringResource(id = R.string.next),
                        color = White,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                coroutineScope.launch { state.animateScrollToPage(state.currentPage + 1) }
                            }
                    )
                }
            }
        },
    ) {
        HorizontalPager(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .safeDrawingPadding()
                .padding(bottom = it.calculateBottomPadding())
        ) { page ->
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(onBoardingAnimation[page])
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                val animationSize = if (isLandscape) componentSizeByScreen(150.dp) else componentSizeByScreen(250.dp)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(animationSize)
                ) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever
                    )
                }

                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(id = onBoardingTitle[page]),
                    color = White,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (page == pageCount - 1) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val userManager = UserManager(navHostController.context.dataStoreUser)
                                userManager.storeToDataStore(true)
                                navHostController.popBackStack()
                                navHostController.navigate(Destination.Login.route)
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(width = 2.dp, color = White),
                        colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                        modifier = Modifier
                            .fillMaxWidth(if (isLandscape) 0.5f else 0.8f)
                            .height(60.dp)
                    ) {
                        Text(
                            style = MaterialTheme.typography.labelMedium,
                            text = stringResource(id = R.string.getStarted),
                            color = White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}