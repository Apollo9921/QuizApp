package com.apollo9921.quizrise.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.apollo9921.quizrise.presentation.core.White
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen

@Composable
fun TopBar(
    backgroundColor: Color,
    title: String = "",
    isBackEnabled: Boolean,
    onBackClicked: () -> Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isBackEnabled) {
                Box(
                    modifier = Modifier
                        .size(componentSizeByScreen(baseSize = 50.dp))
                        .background(White.copy(alpha = 0.1f), RoundedCornerShape(24.dp)),
                ) {
                    IconButton(
                        onClick = { onBackClicked() },
                        modifier = Modifier.size(componentSizeByScreen(baseSize = 48.dp))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = White,
                            modifier = Modifier.size(componentSizeByScreen(baseSize = 24.dp))
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            if (title.isNotEmpty()) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = title,
                    color = White
                )
            }
        }
    }
}