package com.example.quizapp.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quizapp.view.custom.*
import com.example.quizapp.view.navigation.Destination
import com.example.quizapp.view.theme.Black
import com.example.quizapp.view.theme.White

@SuppressLint("StaticFieldLeak")
private lateinit var context: Context

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Quiz(navHostController: NavHostController) {
    count = 0
    isLoadedUser.value = false
    isLoadedResults.value = false
    totalPoints = 0
    totalPointsPossible = 0
    correctAnswersBefore = 0
    incorrectAnswersBefore = 0
    context = LocalContext.current
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 20.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(20.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(categories.size) {
            Card(
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(width = 2.dp, color = White),
                colors = CardDefaults.cardColors(
                    containerColor = White,
                    contentColor = Black,
                    disabledContainerColor = White,
                    disabledContentColor = Black
                ),
                modifier = Modifier
                    .size(
                        if (mediaQueryWidth() <= small) {
                            150.dp
                        } else if (mediaQueryWidth() <= normal) {
                            250.dp
                        } else {
                            350.dp
                        }
                    )
                    .clickable {
                        navHostController.navigate(
                            Destination.LevelDifficulty.passArgument(
                                context.resources.getString(
                                    categories[it]
                                )
                            )
                        )
                    }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = categoriesImages[it]),
                        contentDescription = null,
                        modifier = Modifier
                            .size(
                                if (mediaQueryWidth() <= small) {
                                    35.dp
                                } else if (mediaQueryWidth() <= normal) {
                                    40.dp
                                } else {
                                    45.dp
                                }
                            )
                    )
                    Text(
                        text = stringResource(id = categories[it]),
                        color = Black,
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
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}