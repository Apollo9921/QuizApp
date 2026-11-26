package com.apollo9921.quizrise.presentation.screens.streakTracker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.presentation.components.TopBar
import com.apollo9921.quizrise.presentation.core.Purple40
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.QuizAppTheme
import com.apollo9921.quizrise.presentation.core.White
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen
import com.apollo9921.quizrise.R

@Composable
fun StreakAndLivesInfoRoute(
    navHostController: NavHostController,
    currentStreak: Int = 0,
    currentLives: Int = 0
) {
    StreakAndLivesInfoScreen(
        currentStreak = currentStreak,
        currentLives = currentLives,
        onBackClick = { navHostController.navigateUp() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreakAndLivesInfoScreen(
    currentStreak: Int,
    currentLives: Int,
    onBackClick: () -> Unit
) {
    val fireColor = Color(0xFFFF9800)
    val heartColor = Color(0xFFF87171)

    Scaffold(
        topBar = {
            TopBar(
                backgroundColor = MaterialTheme.colorScheme.primary,
                title = stringResource(R.string.streak_title),
                onClick = { onBackClick() }
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
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CurrentStatusCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.LocalFireDepartment,
                        iconTint = fireColor,
                        value = stringResource(R.string.streak_days, currentStreak),
                        label = stringResource(R.string.actual_sequence)
                    )
                    CurrentStatusCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Favorite,
                        iconTint = heartColor,
                        value = stringResource(R.string.current_lives, currentLives),
                        label = stringResource(R.string.available)
                    )
                }

                InfoSectionCard(
                    icon = Icons.Default.LocalFireDepartment,
                    iconTint = fireColor,
                    title = stringResource(R.string.sequence_counter_title),
                    description = stringResource(R.string.sequence_counter_description)
                ) {
                    HorizontalDivider(color = White.copy(alpha = 0.1f))
                    Text(
                        text = stringResource(R.string.consistence_title),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = White.copy(alpha = 0.5f)
                    )

                    MilestoneRow(
                        days = 3,
                        reward = stringResource(R.string.one_extra_life),
                        isReached = currentStreak >= 3,
                        heartColor = heartColor
                    )
                    MilestoneRow(
                        days = 7,
                        reward = stringResource(R.string.three_extras_life),
                        isReached = currentStreak >= 7,
                        heartColor = heartColor
                    )
                }

                InfoSectionCard(
                    icon = Icons.Default.Favorite,
                    iconTint = heartColor,
                    title = stringResource(R.string.second_opportunity_title),
                    description = stringResource(R.string.second_opportunity_description)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(White.copy(alpha = 0.08f))
                            .border(1.dp, heartColor.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = heartColor,
                                modifier = Modifier.size(22.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.eureka_help),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = White
                                )
                                Text(
                                    text = stringResource(R.string.use_life),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = White.copy(alpha = 0.75f)
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onBackClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple40,
                        contentColor = White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(componentSizeByScreen(baseSize = 54.dp))
                ) {
                    Text(
                        text = stringResource(R.string.got_it),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CurrentStatusCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconTint: Color,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, White.copy(alpha = 0.15f)),
        colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.06f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(iconTint.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = White
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = White.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun InfoSectionCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    description: String,
    extraContent: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, White.copy(alpha = 0.15f)),
        colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.06f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }

            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = White.copy(alpha = 0.8f),
                textAlign = TextAlign.Start
            )

            extraContent()
        }
    }
}

@Composable
private fun MilestoneRow(
    days: Int,
    reward: String,
    isReached: Boolean,
    heartColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(White.copy(alpha = if (isReached) 0.12f else 0.04f))
            .border(
                width = 1.dp,
                color = if (isReached) Color(0xFF4ADE80).copy(alpha = 0.5f) else White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(componentSizeByScreen(18.dp))
            )
            Text(
                text = stringResource(R.string.consecutive_days, days),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = heartColor,
                modifier = Modifier.size(componentSizeByScreen(16.dp))
            )
            Text(
                text = reward,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = if (isReached) Color(0xFF4ADE80) else White.copy(alpha = 0.85f)
            )
        }
    }
}


@Preview(name = "Portrait", showBackground = true, showSystemUi = true)
@Composable
private fun StreakAndLivesInfoPortraitPreview() {
    QuizAppTheme {
        StreakAndLivesInfoScreen(
            currentStreak = 4,
            currentLives = 1,
            onBackClick = {}
        )
    }
}

@Preview(
    name = "Landscape",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=891dp,height=411dp,orientation=landscape"
)
@Composable
private fun StreakAndLivesInfoLandscapePreview() {
    QuizAppTheme {
        StreakAndLivesInfoScreen(
            currentStreak = 7,
            currentLives = 4,
            onBackClick = {}
        )
    }
}