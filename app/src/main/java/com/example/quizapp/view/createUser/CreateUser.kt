package com.example.quizapp.view.createUser

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.quizapp.R
import com.example.quizapp.model.results.Results
import com.example.quizapp.model.user.User
import com.example.quizapp.view.custom.*
import com.example.quizapp.view.main.userManager
import com.example.quizapp.view.navigation.Destination
import com.example.quizapp.view.theme.Black
import com.example.quizapp.view.theme.Purple40
import com.example.quizapp.view.theme.PurpleGrey40
import com.example.quizapp.view.theme.White
import com.example.quizapp.viewModel.ResultsViewModel
import com.example.quizapp.viewModel.UserViewModel
import kotlinx.coroutines.runBlocking

private var name = mutableStateOf("")
private var create = mutableStateOf(false)
private lateinit var userViewModel: UserViewModel
private lateinit var resultsViewModel: ResultsViewModel

@Composable
fun CreateUser(navHostController: NavHostController) {
    userViewModel = UserViewModel(LocalContext.current)
    resultsViewModel = ResultsViewModel(LocalContext.current)
    if(create.value) {
        create.value = false
        val user = User(
            name = name.value,
            totalPoints = 0,
            totalPointsPossible = 0,
            badge = stringResource(id = badgesDescription[0])
        )
        userViewModel.createUser(user)
        for (i in categories.indices) {
            val results = Results(
                category = LocalContext.current.resources.getString(categories[i]),
                correctAnswers = 0,
                incorrectAnswers = 0
            )
            resultsViewModel.createCategory(results)
        }
        runBlocking { userManager.storeToDataStore(true, name.value) }
        navHostController.popBackStack()
        navHostController.navigate(Destination.Main.route)
    }

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
                modifier = Modifier
                    .size(
                        if (mediaQueryWidth() <= small) {
                            200.dp
                        } else  if (mediaQueryWidth() <= normal) {
                            300.dp
                        } else {
                            400.dp
                        }
                    )
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
                      text = stringResource(id = R.string.insertName),
                      color = Black,
                      fontSize =
                          if (mediaQueryWidth() <= small) {
                              16.sp
                          } else if (mediaQueryWidth() <= normal) {
                              20.sp
                          } else {
                              24.sp
                          },
                        fontFamily = FontFamily.SansSerif
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 40.dp)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Button(
                onClick = {
                    if(name.value.isNotBlank()) {
                        create.value = true
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
                    text = stringResource(id = R.string.createUser),
                    color = White,
                    fontSize =
                    if (mediaQueryWidth() <= small) {
                        20.sp
                    } else if (mediaQueryWidth() <= normal) {
                        25.sp
                    } else {
                        30.sp
                    },
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}