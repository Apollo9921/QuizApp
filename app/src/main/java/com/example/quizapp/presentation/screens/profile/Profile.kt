package com.example.quizapp.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.quizapp.R
import com.example.quizapp.domain.model.user.User
import com.example.quizapp.presentation.components.BottomNavigationBar
import com.example.quizapp.presentation.core.Black
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.White
import com.example.quizapp.presentation.utils.formatTotalCount
import com.example.quizapp.presentation.utils.mediaQueryWidth
import com.example.quizapp.presentation.utils.normal
import com.example.quizapp.presentation.utils.small
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileRoute(
    navHostController: NavHostController,
    viewModel: ProfileViewModel = koinViewModel<ProfileViewModel>()
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState().value
    val badgeState = viewModel.badgeState.collectAsState().value
    val fetchUser = { viewModel.fetchUser(context) }

    ProfileScreen(navHostController, uiState, badgeState, fetchUser)
}

@Composable
private fun ProfileScreen(
    navHostController: NavHostController,
    uiState: ProfileViewModel.UIState,
    badgeState: ProfileViewModel.Badge,
    fetchUser: () -> Unit
) {
    LaunchedEffect(Unit) {
        fetchUser()
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .padding(bottom = it.calculateBottomPadding())
        ) {
            when (uiState) {
                is ProfileViewModel.UIState.Success -> {
                    val user = uiState.user
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://api.dicebear.com/5.x/adventurer/png?seed=${user.name}&backgroundColor=transparent")
                            .placeholder(R.drawable.person)
                            .error(R.drawable.person)
                            .build()
                    )
                    ShowProfile(user, painter, badgeState)
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun ShowProfile(
    user: User,
    painter: AsyncImagePainter,
    badgeState: ProfileViewModel.Badge
) {
    val percentage = (user.totalPoints * 100) / badgeState.badgeLevel.toDouble()

    // Determine screen size category once
    val screenWidth = mediaQueryWidth()
    val isSmall = screenWidth <= small
    val isNormal = screenWidth <= normal

    // Dynamic sizes based on screen width
    val topPadding = if (isSmall) 220.dp else if (isNormal) 320.dp else 420.dp
    val iconSize = if (isSmall) 48.dp else if (isNormal) 64.dp else 80.dp

    Box(modifier = Modifier.fillMaxSize()) {
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding - 40.dp)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(White)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = user.name,
                    color = Black,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(32.dp))

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ProfileInfoRow(
                        iconRes = R.drawable.points,
                        label = stringResource(R.string.points),
                        value = formatTotalCount(user.totalPoints.toFloat()),
                        iconSize = iconSize
                    )

                    ProfileInfoRow(
                        iconRes = R.drawable.daily,
                        label = stringResource(R.string.progressPercentage),
                        value = "${percentage.toInt()}%",
                        iconSize = iconSize
                    )

                    ProfileInfoRow(
                        iconRes = badgeState.badge,
                        label = stringResource(R.string.badge),
                        value = user.badge,
                        iconSize = iconSize,
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
    iconSize: Dp,
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
            )

            Text(
                style = MaterialTheme.typography.labelMedium,
                text = label,
                modifier = Modifier.weight(1f),
                color = Black.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Start
            )

            Text(
                style = MaterialTheme.typography.labelSmall,
                text = value,
                color = Black,
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