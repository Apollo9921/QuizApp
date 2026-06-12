package com.example.quizapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.White

@Composable
fun Loading(message: String = "") {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PurpleGrey40
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = White,
                strokeWidth = 4.dp,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    color = White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}