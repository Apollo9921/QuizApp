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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.presentation.components.TopBar
import com.apollo9921.quizrise.presentation.core.Black
import com.apollo9921.quizrise.presentation.core.QuizAppTheme
import com.apollo9921.quizrise.presentation.navigation.Destination
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen

@Composable
fun LevelDifficulty(navHostController: NavHostController, category: String) {
    val context = LocalContext.current
    val levelsDifficulty = listOf(
        R.string.easy_translatable,
        R.string.medium_translatable,
        R.string.hard_translatable
    )

    var selectedOption by rememberSaveable { mutableIntStateOf(levelsDifficulty[0]) }
    var isRandomMode by rememberSaveable { mutableStateOf(false) }

    val cardHeight = componentSizeByScreen(baseSize = 72.dp)

    Scaffold(
        topBar = {
            TopBar(
                backgroundColor = MaterialTheme.colorScheme.primary,
                onClick = { navHostController.navigateUp() })
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

                Text(
                    text = stringResource(id = R.string.chooseLevel),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.surface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.choose_level_difficulty),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(componentSizeByScreen(baseSize = 32.dp)))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { isRandomMode = !isRandomMode }
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.05f))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.random),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.surface,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(id = R.string.random_system),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                        )
                    }
                    Switch(
                        checked = isRandomMode,
                        onCheckedChange = { isRandomMode = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.secondary,
                            checkedTrackColor = MaterialTheme.colorScheme.surface,
                            uncheckedThumbColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            uncheckedTrackColor = Black.copy(alpha = 0.2f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    levelsDifficulty.forEach { item ->
                        DifficultyCard(
                            title = stringResource(id = item),
                            isSelected = (selectedOption == item) && !isRandomMode,
                            isRandomModeActive = isRandomMode,
                            onClick = {
                                selectedOption = item
                                isRandomMode = false
                            },
                            modifier = Modifier.height(cardHeight)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .navigationBarsPadding(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 24.dp)
                    ) {
                        Button(
                            onClick = {
                                val finalResId =
                                    if (isRandomMode) levelsDifficulty.random() else selectedOption
                                val finalLevelString = context.resources.getString(finalResId)

                                navHostController.navigate(
                                    Destination.StartQuiz.passArgument(
                                        category,
                                        finalLevelString
                                    )
                                )
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
                                text = stringResource(id = R.string.getStarted),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.surface
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
    isRandomModeActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(
            alpha = 0.1f
        )
    val textColor = if (isSelected) Black else MaterialTheme.colorScheme.surface
    val borderStroke = if (isSelected) null else BorderStroke(
        1.dp,
        MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
    )
    val indicatorSize = componentSizeByScreen(baseSize = 24.dp)

    val cardAlpha = if (isRandomModeActive) 0.4f else 1f

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .alpha(cardAlpha)
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
                        color = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent,
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface.copy(
                            alpha = 0.6f
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(indicatorSize * 0.4f)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LevelDifficultyPreview() {
    QuizAppTheme {
        LevelDifficulty(rememberNavController(), "General Knowledge")
    }
}