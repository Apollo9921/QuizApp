package com.apollo9921.quizrise.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apollo9921.quizrise.presentation.core.QuizAppTheme
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen

@Composable
fun StreakAndLivesTracker(
    streakDays: Int,
    livesCount: Int,
    onTrackerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fireColor = if (streakDays > 0) Color(0xFFFF9800) else MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
    val heartColor = if (livesCount > 0) Color(0xFFF87171) else MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)

    Row(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxWidth()
            .padding(top = 12.dp, end = 24.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box {
            Surface(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onTrackerClick() },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surface.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = componentSizeByScreen(baseSize = 14.dp),
                        vertical = componentSizeByScreen(baseSize = 8.dp)
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = fireColor,
                            modifier = Modifier.size(componentSizeByScreen(baseSize = 20.dp))
                        )
                        Text(
                            text = streakDays.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(componentSizeByScreen(baseSize = 14.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f))
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = heartColor,
                            modifier = Modifier.size(componentSizeByScreen(baseSize = 18.dp))
                        )
                        Text(
                            text = livesCount.toString(),
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


@Preview(name = "Active (With Streak And Lives)", showBackground = true)
@Composable
private fun StreakAndLivesTrackerActivePreview() {
    QuizAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(bottom = 24.dp)
        ) {
            StreakAndLivesTracker(
                streakDays = 7,
                livesCount = 3,
                onTrackerClick = {}
            )
        }
    }
}

@Preview(name = "Inactive (0 Days / 0 Lives)", showBackground = true)
@Composable
private fun StreakAndLivesTrackerEmptyPreview() {
    QuizAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(bottom = 24.dp)
        ) {
            StreakAndLivesTracker(
                streakDays = 0,
                livesCount = 0,
                onTrackerClick = {}
            )
        }
    }
}