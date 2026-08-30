package com.apollo9921.quizrise.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.presentation.core.White

data class CategoryStat(val name: Int, val percentage: Int)

data class ShareCardData(
    val totalPoints: Int,
    val overallAccuracy: Int,
    val categories: List<CategoryStat>
)

@Composable
fun QuizResultShareCard(
    data: ShareCardData,
    modifier: Modifier = Modifier
) {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1E1035), Color(0xFF0F081D))
    )
    val accentGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF8A2387), Color(0xFFE94057), Color(0xFFF27121))
    )
    val cardBackground = Color(0xFF2A1B4E).copy(alpha = 0.6f)
    val cyanNeon = Color(0xFF00F2FE)

    Box(
        modifier = modifier
            .width(360.dp)
            .wrapContentHeight()
            .background(backgroundGradient)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(accentGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        style = MaterialTheme.typography.labelMedium,
                        text = "⚡"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(R.string.app_name),
                    fontWeight = FontWeight.Black,
                    color = White,
                    letterSpacing = 2.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, White.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        style = MaterialTheme.typography.displaySmall,
                        text = stringResource(R.string.total_points).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = White.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${data.totalPoints}",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = cyanNeon
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(accentGradient)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            style = MaterialTheme.typography.displaySmall,
                            text = stringResource(R.string.average_points, data.overallAccuracy),
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.performance_top_categories).uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = White.copy(alpha = 0.5f),
                    letterSpacing = 1.sp
                )
                val bestCategories = data.categories
                    .sortedByDescending { it.percentage }
                    .take(3)
                bestCategories.forEach { cat ->
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                style = MaterialTheme.typography.displaySmall,
                                text = stringResource(cat.name),
                                color = White,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                style = MaterialTheme.typography.displaySmall,
                                text = "${cat.percentage}%",
                                color = cyanNeon,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        LinearProgressIndicator(
                            progress = { cat.percentage / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = cyanNeon,
                            trackColor = White.copy(alpha = 0.1f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(White.copy(alpha = 0.05f))
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    style = MaterialTheme.typography.displaySmall,
                    text = stringResource(R.string.can_you_beat_me),
                    color = White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    style = MaterialTheme.typography.displaySmall,
                    text = stringResource(R.string.app_name),
                    color = cyanNeon,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizResultShareCardPreview() {
    val mockData = ShareCardData(
        totalPoints = 2450,
        overallAccuracy = 84,
        categories = listOf(
            CategoryStat(R.string.artsAndLiterature_translatable, 92),
            CategoryStat(R.string.history_translatable, 78),
            CategoryStat(R.string.sportAndLeisure_translatable, 85)
        )
    )

    QuizResultShareCard(data = mockData)
}