package com.apollo9921.quizrise.presentation.screens.profile

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.presentation.components.BottomNavigationBar
import com.apollo9921.quizrise.presentation.components.SettingsDialog
import com.apollo9921.quizrise.presentation.core.PurpleGrey40
import com.apollo9921.quizrise.presentation.core.White
import com.apollo9921.quizrise.presentation.core.getTypography
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen
import com.apollo9921.quizrise.presentation.utils.formatTotalCount
import org.koin.androidx.compose.koinViewModel
import com.apollo9921.quizrise.R

@Composable
fun ProfileRoute(
    navHostController: NavHostController,
    viewModel: ProfileViewModel = koinViewModel<ProfileViewModel>()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val badgeState = viewModel.badgeState.collectAsStateWithLifecycle().value
    val fetchUser = { viewModel.fetchUser() }

    LaunchedEffect(Unit) {
        fetchUser()
    }
    var showSettings by remember { mutableStateOf(false) }

    ProfileScreen(
        navHostController = navHostController,
        uiState = uiState,
        badgeState = badgeState,
        onSettingsClick = { showSettings = true }
    )

    if (showSettings) {
        SettingsDialog(
            onDismissRequest = { showSettings = false },
            onPrivacyPolicyClick = {
                // TODO: Open Privacy Policy Link
            },
            onLogoutClick = {
                showSettings = false
                // TODO: Call ViewModel to logout the session
            }
        )
    }
}

@Composable
private fun ProfileScreen(
    navHostController: NavHostController,
    uiState: ProfileViewModel.UIState,
    badgeState: ProfileViewModel.Badge,
    onSettingsClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController) },
        containerColor = PurpleGrey40
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
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
                else -> {}
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

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val topPadding = if (isLandscape) componentSizeByScreen(baseSize = 120.dp) else componentSizeByScreen(baseSize = 240.dp)
    val iconContainerSize = componentSizeByScreen(baseSize = 44.dp)
    val badgeIconSize = componentSizeByScreen(baseSize = 60.dp)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .statusBarsPadding()
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 24.dp)
                    .size(componentSizeByScreen(baseSize = 40.dp))
                    .background(White.copy(alpha = 0.12f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = White,
                    modifier = Modifier.size(componentSizeByScreen(baseSize = 24.dp))
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
                    modifier = Modifier.fillMaxSize(if (isLandscape) 0.8f else 0.65f),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = topPadding)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(PurpleGrey40)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(componentSizeByScreen(baseSize = 16.dp)))

                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(componentSizeByScreen(baseSize = 24.dp)))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = painterResource(id = R.drawable.points),
                        title = stringResource(R.string.points),
                        value = formatTotalCount(user.totalPoints.toFloat()),
                        iconContainerSize = iconContainerSize
                    )

                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = painterResource(id = R.drawable.daily),
                        title = stringResource(R.string.progressPercentage),
                        value = "$displayPercentage%",
                        iconContainerSize = iconContainerSize
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                BadgeCard(
                    badgeIcon = painterResource(id = badgeState.badge),
                    badgeName = user.badge,
                    progress = percentage.toFloat(),
                    badgeIconSize = badgeIconSize
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
    iconContainerSize: Dp
) {
    Card(
        modifier = modifier,
        border = BorderStroke(width = 1.dp, color = White.copy(alpha = 0.15f)),
        colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.06f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(componentSizeByScreen(baseSize = 16.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(iconContainerSize)
                    .background(White.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = icon,
                    colorFilter = ColorFilter.tint(White),
                    contentDescription = null,
                    modifier = Modifier.size(iconContainerSize * 0.55f)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = White.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium,
                color = White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BadgeCard(
    badgeIcon: Painter,
    badgeName: String,
    progress: Float,
    badgeIconSize: Dp
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = White.copy(alpha = 0.15f)),
        colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.06f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(componentSizeByScreen(baseSize = 16.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = badgeIcon,
                contentDescription = "Badge Icon",
                modifier = Modifier.size(badgeIconSize)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(R.string.badge).uppercase(),
                    style = MaterialTheme.typography.displaySmall,
                    color = White.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = badgeName,
                    style = MaterialTheme.typography.labelMedium,
                    color = White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(componentSizeByScreen(baseSize = 8.dp))
                        .clip(CircleShape),
                    color = White,
                    trackColor = White.copy(alpha = 0.15f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenDarkPreview() {
    val mockUser = User(name = "Golden Hercules", totalPoints = 750, badge = "Newbie")
    val mockBadge = ProfileViewModel.Badge(badge = R.drawable.newbie, badgeLevel = 1000)

    MaterialTheme(typography = getTypography()) {
        Box(modifier = Modifier.fillMaxSize().background(PurpleGrey40)) {
            ShowProfile(
                user = mockUser,
                painter = rememberAsyncImagePainter(model = ""),
                badgeState = mockBadge,
                onSettingsClick = {}
            )
        }
    }
}