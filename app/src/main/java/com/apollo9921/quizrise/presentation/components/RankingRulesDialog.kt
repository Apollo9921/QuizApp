package com.apollo9921.quizrise.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.apollo9921.quizrise.domain.util.PlayerLevel
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.White
import com.apollo9921.quizrise.R

@Composable
fun RankingRulesDialog(
    isVisible: Boolean,
    onDismissRequest: () -> Unit
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
                    .padding(24.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, White.copy(alpha = 0.15f)),
                colors = CardDefaults.cardColors(
                    containerColor = PurpleGrey40
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.leaderboard_title),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                        IconButton(onClick = onDismissRequest) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = White.copy(alpha = 0.7f)
                            )
                        }
                    }

                    HorizontalDivider(color = White.copy(alpha = 0.1f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(White.copy(alpha = 0.05f))
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.level).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = White.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = stringResource(R.string.score).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = White.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    HorizontalDivider(color = White.copy(alpha = 0.1f))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(PlayerLevel.getAllLevels()) { rule ->
                            RankingRowItem(rule)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun RankingRowItem(rule: PlayerLevel) {
    val pointsText = if (rule.maxPoints == Int.MAX_VALUE) {
        "${rule.minPoints}+ pts"
    } else {
        "${rule.minPoints} - ${rule.maxPoints} pts"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(rule.resourceId),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = White,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = pointsText,
            style = MaterialTheme.typography.labelMedium,
            color = White.copy(alpha = 0.8f),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewRankingRulesDialog() {
    RankingRulesDialog(isVisible = true, onDismissRequest = {})
}