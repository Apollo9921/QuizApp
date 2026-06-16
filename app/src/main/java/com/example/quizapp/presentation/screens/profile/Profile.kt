package com.example.quizapp.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.quizapp.presentation.utils.normal
import com.example.quizapp.presentation.utils.small
import com.example.quizapp.presentation.utils.widthOfScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileRoute(
    navHostController: NavHostController,
    viewModel: ProfileViewModel = koinViewModel<ProfileViewModel>()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val badgeState = viewModel.badgeState.collectAsState().value
    val fetchUser = { viewModel.fetchUser() }

    LaunchedEffect(Unit) {
        fetchUser()
    }

    ProfileScreen(
        navHostController = navHostController,
        uiState = uiState,
        badgeState = badgeState,
        onSettingsClick = {
            //TODO NAVIGATE TO SETTINGS
        }
    )
}

@Composable
private fun ProfileScreen(
    navHostController: NavHostController,
    uiState: ProfileViewModel.UIState,
    badgeState: ProfileViewModel.Badge,
    onSettingsClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .padding(bottom = paddingValues.calculateBottomPadding())
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
                    ShowProfile(user, painter, badgeState, onSettingsClick)
                }
                else -> {

                }
            }
        }
    }
}

@Composable
private fun ShowProfile(
    user: User,
    painter: AsyncImagePainter,
    badgeState: ProfileViewModel.Badge,
    onSettingsClick: () -> Unit
) {
    val percentage = ((user.totalPoints * 100) / badgeState.badgeLevel.toDouble()) / 100.0
    val displayPercentage = (percentage * 100).toInt()

    val screenWidth = widthOfScreen()
    val isSmall = screenWidth <= small
    val isNormal = screenWidth <= normal

    val topPadding = if (isSmall) 200.dp else if (isNormal) 280.dp else 360.dp

    Box(modifier = Modifier.fillMaxSize()) {

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .statusBarsPadding()
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 16.dp)
                .background(White.copy(alpha = 0.2f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = White
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(topPadding),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = "Profile Avatar",
                modifier = Modifier.fillMaxSize(0.65f),
                contentScale = ContentScale.Fit
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding - 20.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(White)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = user.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = painterResource(id = R.drawable.points),
                        title = stringResource(R.string.points),
                        value = formatTotalCount(user.totalPoints.toFloat()),
                        accentColor = PurpleGrey40.copy(alpha = 0.08f)
                    )

                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = painterResource(id = R.drawable.daily),
                        title = stringResource(R.string.progressPercentage),
                        value = "$displayPercentage%",
                        accentColor = PurpleGrey40.copy(alpha = 0.08f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                BadgeCard(
                    badgeIcon = painterResource(id = badgeState.badge),
                    badgeName = user.badge,
                    progress = percentage.toFloat()
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String,
    value: String,
    accentColor: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = accentColor),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Black.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BadgeCard(
    badgeIcon: Painter,
    badgeName: String,
    progress: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = badgeIcon,
                contentDescription = "Badge Icon",
                modifier = Modifier.size(60.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(R.string.badge).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = PurpleGrey40,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = badgeName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Black,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = PurpleGrey40,
                    trackColor = PurpleGrey40.copy(alpha = 0.15f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    val mockUser = User(
        name = "Golden Hercules",
        totalPoints = 750,
        badge = "Newbie"
    )

    val mockBadge = ProfileViewModel.Badge(
        badge = R.drawable.newbie,
        badgeLevel = 1000
    )

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
        ) {
            val dummyPainter = rememberAsyncImagePainter(model = "")

            ShowProfile(
                user = mockUser,
                painter = dummyPainter,
                badgeState = mockBadge,
                onSettingsClick = {}
            )
        }
    }
}