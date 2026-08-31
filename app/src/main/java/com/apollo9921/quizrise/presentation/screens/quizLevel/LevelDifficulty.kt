package com.apollo9921.quizrise.presentation.screens.quizLevel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.apollo9921.quizrise.presentation.components.TopBar
import com.apollo9921.quizrise.presentation.core.Black
import com.apollo9921.quizrise.presentation.core.Purple40
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.White
import com.apollo9921.quizrise.presentation.navigation.Destination
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen
import com.apollo9921.quizrise.presentation.utils.widthOfScreen
import com.apollo9921.quizrise.R

@Composable
fun LevelDifficulty(navHostController: NavHostController, category: String) {
    val context = LocalContext.current
    val levelsDifficulty = listOf(
        R.string.easy_translatable,
        R.string.medium_translatable,
        R.string.hard_translatable
    )

    var selectedOption by rememberSaveable { mutableIntStateOf(levelsDifficulty[0]) }
    val level = rememberSaveable { mutableStateOf("") }

    val screenWidth = widthOfScreen()
    val maxLayoutWidth = if (screenWidth < 600.dp) Dp.Unspecified else componentSizeByScreen(560.dp)
    val cardHeight = componentSizeByScreen(baseSize = 72.dp)

    LaunchedEffect(selectedOption) {
        level.value = context.resources.getString(selectedOption)
    }

    Scaffold(
        topBar = {
            TopBar(
                backgroundColor = PurpleGrey40,
                onClick = { navHostController.navigateUp() })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = maxLayoutWidth)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.chooseLevel),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.choose_level_difficulty),
                    style = MaterialTheme.typography.labelSmall,
                    color = White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(componentSizeByScreen(baseSize = 40.dp)))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    levelsDifficulty.forEach { item ->
                        DifficultyCard(
                            title = stringResource(id = item),
                            isSelected = (selectedOption == item),
                            onClick = { selectedOption = item },
                            modifier = Modifier.height(cardHeight)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PurpleGrey40)
                        .navigationBarsPadding(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = maxLayoutWidth)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 24.dp)
                    ) {
                        Button(
                            onClick = {
                                navHostController.navigate(
                                    Destination.StartQuiz.passArgument(
                                        category,
                                        level.value
                                    )
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Purple40,
                                contentColor = White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(componentSizeByScreen(baseSize = 56.dp))
                        ) {
                            Text(
                                text = stringResource(id = R.string.getStarted),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DifficultyCard(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isSelected) White else White.copy(alpha = 0.1f)
    val textColor = if (isSelected) Black else White
    val borderStroke = if (isSelected) null else BorderStroke(1.dp, White.copy(alpha = 0.2f))
    val indicatorSize = componentSizeByScreen(baseSize = 24.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        color = containerColor,
        shape = RoundedCornerShape(20.dp),
        border = borderStroke
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Box(
                modifier = Modifier
                    .size(indicatorSize)
                    .background(
                        color = if (isSelected) Purple40 else Color.Transparent,
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = if (isSelected) Purple40 else White.copy(alpha = 0.6f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(indicatorSize * 0.4f)
                            .background(White, CircleShape)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LevelDifficultyPreview() {
    LevelDifficulty(rememberNavController(), "General Knowledge")
}