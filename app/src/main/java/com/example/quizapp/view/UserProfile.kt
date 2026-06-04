package com.example.quizapp.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.quizapp.R
import com.example.quizapp.data.local.database.QuizDatabase
import com.example.quizapp.data.local.entity.UserEntity
import com.example.quizapp.presentation.components.BottomNavigationBar
import com.example.quizapp.view.custom.*
import com.example.quizapp.presentation.userName
import com.example.quizapp.presentation.core.Black
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.White
import com.example.quizapp.viewModel.UserViewModel
import org.koin.androidx.compose.koinViewModel

private lateinit var user: SnapshotStateList<UserEntity>
private var badge = 0
private var badgeLevel = 0

@SuppressLint("StaticFieldLeak")
private lateinit var context: Context

@Composable
fun UserProfile(
    navHostController: NavHostController,
    viewModel: UserViewModel = koinViewModel<UserViewModel>()
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .padding(bottom = it.calculateBottomPadding())
        ) {
            user = remember { mutableStateListOf() }
            context = LocalContext.current
            QuizDatabase.getDatabase(context)
                .userDao().fetchUserProfile()
                .observe(LocalLifecycleOwner.current) {
                    for (i in badgesPoints.indices) {
                        if (it.totalPoints <= badgesPoints[i]) {
                            badgeLevel = badgesPoints[i]
                            break
                        }
                    }
                    when (it.badge) {
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
                                viewModel.updateBadge(
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
                    model = ImageRequest.Builder(context)
                        .data("https://api.dicebear.com/5.x/adventurer/png?seed=${user[0].name}&backgroundColor=transparent")
                        .placeholder(R.drawable.person)
                        .error(R.drawable.person)
                        .build()
                )
                ShowProfile(user, painter)
            }
        }
    }
}

@Composable
private fun ShowProfile(user: SnapshotStateList<UserEntity>, painter: AsyncImagePainter) {
    val percentage = (user[0].totalPoints * 100) / badgeLevel.toDouble()

    // Determine screen size category once
    val screenWidth = mediaQueryWidth()
    val isSmall = screenWidth <= small
    val isNormal = screenWidth <= normal

    // Dynamic sizes based on screen width
    val topPadding = if (isSmall) 220.dp else if (isNormal) 320.dp else 420.dp
    val iconSize = if (isSmall) 48.dp else if (isNormal) 64.dp else 80.dp
    val titleFontSize = if (isSmall) 28.sp else if (isNormal) 32.sp else 40.sp
    val labelFontSize = if (isSmall) 20.sp else if (isNormal) 24.sp else 30.sp
    val valueFontSize = if (isSmall) 18.sp else if (isNormal) 22.sp else 28.sp

    Box(modifier = Modifier.fillMaxSize()) {
        // Avatar Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(topPadding),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = "Profile Avatar",
                modifier = Modifier.fillMaxSize(0.7f),
                contentScale = ContentScale.Fit
            )
        }

        // Details Sheet
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding - 40.dp) // Overlap slightly with avatar area
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(White)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))

                // User Name
                Text(
                    text = user[0].name,
                    color = Black,
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Info Section
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ProfileInfoRow(
                        iconRes = R.drawable.points,
                        label = stringResource(R.string.points),
                        value = formatTotalCount(user[0].totalPoints.toFloat()),
                        iconSize = iconSize,
                        labelSize = labelFontSize,
                        valueSize = valueFontSize
                    )

                    ProfileInfoRow(
                        iconRes = R.drawable.daily,
                        label = stringResource(R.string.progressPercentage),
                        value = "${percentage.toInt()}%",
                        iconSize = iconSize,
                        labelSize = labelFontSize,
                        valueSize = valueFontSize
                    )

                    ProfileInfoRow(
                        iconRes = badge, // Global badge variable
                        label = stringResource(R.string.badge),
                        value = user[0].badge,
                        iconSize = iconSize,
                        labelSize = labelFontSize,
                        valueSize = valueFontSize,
                        showDivider = false
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(
    iconRes: Int,
    label: String,
    value: String,
    iconSize: androidx.compose.ui.unit.Dp,
    labelSize: androidx.compose.ui.unit.TextUnit,
    valueSize: androidx.compose.ui.unit.TextUnit,
    showDivider: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                // Only tint if it's not the badge (assuming badge is colorful)
                colorFilter = if (iconRes != badge) ColorFilter.tint(Black) else null
            )

            Text(
                text = label,
                modifier = Modifier.weight(1f),
                color = Black.copy(alpha = 0.6f),
                fontSize = labelSize,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = value,
                color = Black,
                fontSize = valueSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
        if (showDivider) {
            HorizontalDivider(
                thickness = 1.dp,
                color = Black.copy(alpha = 0.1f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}