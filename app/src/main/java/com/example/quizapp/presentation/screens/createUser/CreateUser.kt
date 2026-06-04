package com.example.quizapp.presentation.screens.createUser

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.quizapp.R
import com.example.quizapp.presentation.core.Black
import com.example.quizapp.presentation.core.Purple40
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.White
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateUserRoute(
    navHostController: NavHostController,
    viewModel: CreateUserViewModel = koinViewModel<CreateUserViewModel>()
) {
    val context = LocalContext.current
    val startCreation = { name: String -> viewModel.startCreation(context, name, navHostController) }

    CreateUser(startCreation)
}

@Composable
private fun CreateUser(startCreation: (String) -> Unit) {
    val name = remember { mutableStateOf("") }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://api.dicebear.com/5.x/adventurer/png?seed=${name.value}&backgroundColor=transparent")
            .placeholder(R.drawable.person)
            .error(R.drawable.person)
            .build()
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.7f),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.padding(10.dp))
            TextField(
                value = name.value,
                onValueChange = {
                    name.value = it
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                placeholder = {
                    Text(
                        style = MaterialTheme.typography.labelSmall,
                        text = stringResource(id = R.string.insertName),
                        color = Black
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 40.dp)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Button(
                onClick = {
                    if (name.value.isNotBlank()) {
                        startCreation(name.value)
                    }
                },
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
                    style = MaterialTheme.typography.labelMedium,
                    text = stringResource(id = R.string.createUser),
                    color = White,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}