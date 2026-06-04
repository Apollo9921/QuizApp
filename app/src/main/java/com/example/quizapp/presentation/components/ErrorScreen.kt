package com.example.quizapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.quizapp.R
import com.example.quizapp.presentation.core.Purple40
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.White

@Composable
fun ErrorScreen(errorMessage: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            text = errorMessage,
            color = White
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Button(
            onClick = { onClick() },
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(width = 2.dp, color = White),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple40,
                contentColor = Purple40,
                disabledContentColor = Purple40,
                disabledContainerColor = Purple40
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp)
        ) {
            Text(
                text = stringResource(id = R.string.try_again),
                color = White,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}