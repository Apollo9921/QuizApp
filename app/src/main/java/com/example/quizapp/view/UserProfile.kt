package com.example.quizapp.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.quizapp.R
import com.example.quizapp.model.database.QuizDatabase
import com.example.quizapp.model.user.User
import com.example.quizapp.view.custom.*
import com.example.quizapp.view.main.userName
import com.example.quizapp.view.theme.Black
import com.example.quizapp.view.theme.White
import com.example.quizapp.viewModel.UserViewModel

private lateinit var user: SnapshotStateList<User>
private var badge = 0
private var badgeLevel = 0
@SuppressLint("StaticFieldLeak")
private lateinit var context: Context

private lateinit var userViewModel: UserViewModel

@Composable
fun UserProfile() {
    user = remember { mutableStateListOf() }
    context = LocalContext.current
    userViewModel = UserViewModel(context)
    QuizDatabase.getDatabase(context)
        .userDao().getUserProfile()
        .observe(LocalLifecycleOwner.current) {
            for (i in badgesPoints.indices) {
                if (it.totalPoints <= badgesPoints[i]) {
                    badgeLevel = badgesPoints[i]
                    break
                }
            }
            when(it.badge) {
                context.resources.getString(R.string.newbie) -> {
                    badge = badges[0]
                }
                context.resources.getString(R.string.intermediate) -> {
                    badge = badges[1]
                }
                context.resources.getString(R.string.advanced) -> {
                    badge = badges[2]
                }
                context.resources.getString(R.string.legend) -> {
                    badge = badges[3]
                }
            }
            for (i in badgesPoints.indices) {
                if (it.totalPoints > badgesPoints[i] && badge == badges[i]) {
                    if (i < badgesPoints.size - 1) {
                        userViewModel.updateBadge(
                            context.getString(badgesDescription[i + 1]),
                            userName
                        )
                        break
                    }
                }
            }
            if (user.isNotEmpty()) {
                user.clear()
            }
            user.add(it)
        }
    if (user.isNotEmpty()) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://api.dicebear.com/5.x/adventurer/png?seed=${user[0].name}&backgroundColor=transparent")
                .placeholder(R.drawable.person)
                .error(R.drawable.person)
                .build()
        )
        ShowProfile(painter)
    }
}

@Composable
private fun ShowProfile(painter: AsyncImagePainter) {
    val percentage = (user[0].totalPoints * 100) / badgeLevel.toDouble()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(
                    if (mediaQueryWidth() <= small) {
                        250.dp
                    } else if (mediaQueryWidth() <= normal) {
                        350.dp
                    } else {
                        450.dp
                    }
                )
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 10.dp,
                end = 10.dp,
                top =
                if (mediaQueryWidth() <= small) {
                    220.dp
                } else if (mediaQueryWidth() <= normal) {
                    320.dp
                } else {
                    420.dp
                }
            )
            .clip(
                RoundedCornerShape(
                    topStart = 50.dp,
                    topEnd = 50.dp
                )
            )
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.padding(20.dp))
            Column {
                Text(
                    text = user[0].name,
                    color = Black,
                    fontSize =
                    if (mediaQueryWidth() <= small) {
                        30.sp
                    } else if (mediaQueryWidth() <= normal) {
                        35.sp
                    } else {
                        45.sp
                    },
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 40.dp)
                )
                Spacer(modifier = Modifier.padding(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp, end = 20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.points),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Black),
                            modifier = Modifier
                                .size(
                                    if (mediaQueryWidth() <= small) {
                                        60.dp
                                    } else if (mediaQueryWidth() <= normal) {
                                        80.dp
                                    } else {
                                        100.dp
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = stringResource(id = R.string.points),
                            color = Black,
                            fontSize =
                            if (mediaQueryWidth() <= small) {
                                35.sp
                            } else if (mediaQueryWidth() <= normal) {
                                45.sp
                            } else {
                                55.sp
                            },
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = formatTotalCount(user[0].totalPoints.toFloat()),
                            color = Black,
                            fontSize =
                            if (mediaQueryWidth() <= small) {
                                35.sp
                            } else if (mediaQueryWidth() <= normal) {
                                45.sp
                            } else {
                                55.sp
                            },
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    HorizontalDivider(
                        color = Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.daily),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Black),
                            modifier = Modifier
                                .size(
                                    if (mediaQueryWidth() <= small) {
                                        60.dp
                                    } else if (mediaQueryWidth() <= normal) {
                                        80.dp
                                    } else {
                                        100.dp
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = stringResource(id = R.string.progressPercentage),
                            color = Black,
                            fontSize =
                            if (mediaQueryWidth() <= small) {
                                35.sp
                            } else if (mediaQueryWidth() <= normal) {
                                45.sp
                            } else {
                                55.sp
                            },
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = "${percentage.toInt()}%",
                            color = Black,
                            fontSize =
                            if (mediaQueryWidth() <= small) {
                                35.sp
                            } else if (mediaQueryWidth() <= normal) {
                                45.sp
                            } else {
                                55.sp
                            },
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    HorizontalDivider(
                        color = Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = badge),
                            contentDescription = null,
                            modifier = Modifier
                                .size(
                                    if (mediaQueryWidth() <= small) {
                                        60.dp
                                    } else if (mediaQueryWidth() <= normal) {
                                        80.dp
                                    } else {
                                        100.dp
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = stringResource(id = R.string.badge),
                            color = Black,
                            fontSize =
                            if (mediaQueryWidth() <= small) {
                                35.sp
                            } else if (mediaQueryWidth() <= normal) {
                                45.sp
                            } else {
                                55.sp
                            },
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = user[0].badge,
                            color = Black,
                            fontSize =
                            if (mediaQueryWidth() <= small) {
                                35.sp
                            } else if (mediaQueryWidth() <= normal) {
                                45.sp
                            } else {
                                55.sp
                            },
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                            lineHeight =
                            if (mediaQueryWidth() <= small) {
                                35.sp
                            } else if (mediaQueryWidth() <= normal) {
                                45.sp
                            } else {
                                55.sp
                            }
                        )
                    }
                }
            }
        }
    }
}