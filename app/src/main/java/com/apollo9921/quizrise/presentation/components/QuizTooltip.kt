package com.apollo9921.quizrise.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.apollo9921.quizrise.presentation.core.Pink40
import com.apollo9921.quizrise.presentation.core.White

@Composable
fun QuizTooltipIcon(text: String) {
    var showTooltip by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box {
            IconButton(
                onClick = { showTooltip = !showTooltip }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Help,
                    contentDescription = "Help",
                    tint = White
                )
            }

            QuizTooltip(
                text = text,
                isVisible = showTooltip,
                onDismissRequest = { showTooltip = false }
            )
        }
    }
}

@Composable
private fun QuizTooltip(
    text: String,
    isVisible: Boolean,
    onDismissRequest: () -> Unit
) {
    if (isVisible) {
        Popup(
            alignment = Alignment.BottomEnd,
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(focusable = true)
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .wrapContentHeight()
                    .padding(top = 16.dp, end = 8.dp, start = 16.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, White.copy(alpha = 0.2f)),
                colors = CardDefaults.cardColors(
                    containerColor = Pink40.copy(alpha = 0.95f)
                )
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelSmall,
                    color = White.copy(alpha = 0.9f),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}