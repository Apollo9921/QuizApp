package com.apollo9921.quizrise.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen

data class Achievement(
    val title: String,
    val iconRes: Int
)

@Composable
fun AchievementsSection(achievements: List<Achievement>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.15f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(
                alpha = 0.06f
            )
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(componentSizeByScreen(baseSize = 12.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.achievements).uppercase(),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = R.string.see_more),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable {  }
                )
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                maxItemsInEachRow = 3,
                verticalArrangement = Arrangement.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                achievements.take(3).forEach {
                    AchievementItem(achievement = it)
                }
            }
        }
    }
}

@Composable
private fun AchievementItem(achievement: Achievement) {
    Column(
        modifier = Modifier
            .width(componentSizeByScreen(baseSize = 110.dp))
            .wrapContentHeight()
            .padding(5.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = achievement.iconRes),
            contentDescription = achievement.title,
            modifier = Modifier
                .size(componentSizeByScreen(baseSize = 110.dp))
        )

        Text(
            text = achievement.title,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.surface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}