package com.example.quizapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quizapp.presentation.core.White

@Composable
fun TopBar(
    backgroundColor: Color,
    title: String,
    isBackEnabled: Boolean,
    onBackClicked: () -> Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding()
            .background(backgroundColor)
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isBackEnabled) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = White,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onBackClicked() }
            )
        }
        Text(
            style = MaterialTheme.typography.titleLarge,
            text = title,
            color = White
        )
    }
}