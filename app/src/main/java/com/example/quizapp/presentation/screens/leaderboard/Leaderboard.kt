package com.example.quizapp.presentation.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.domain.util.PlayerLevel
import com.example.quizapp.domain.util.QuizCategory
import com.example.quizapp.presentation.components.ErrorScreen
import com.example.quizapp.presentation.components.TopBar
import com.example.quizapp.presentation.core.Bronze
import com.example.quizapp.presentation.core.Gold
import com.example.quizapp.presentation.core.LeaderboardAccentColor
import com.example.quizapp.presentation.core.LeaderboardBackground
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.LeaderboardSurfaceColor
import com.example.quizapp.presentation.core.Silver
import com.example.quizapp.presentation.core.White
import com.example.quizapp.presentation.screens.leaderboard.LeaderboardViewModel.LeaderboardItem
import com.example.quizapp.presentation.screens.leaderboard.LeaderboardViewModel.LeaderboardUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun LeaderboardRoute(
    navHostController: NavHostController,
    viewModel: LeaderboardViewModel = koinViewModel<LeaderboardViewModel>()
) {
    val state = viewModel.uiState.collectAsState().value
    val getTopPlayersByLevel = { badge: String -> viewModel.getTopPlayersByLevel(badge) }
    val changeTab = { position: Int -> viewModel.changeTab(position) }
    val filterChanged = { filter: String -> viewModel.changeFilter(filter) }
    val navigateBack = { navHostController.navigateUp() }
    LeaderboardScreenRoute(
        state = state,
        getTopPlayersByLevel = getTopPlayersByLevel,
        changeTab = changeTab,
        filterChanged = filterChanged,
        navigateBack = navigateBack
    )
}

@Composable
private fun LeaderboardScreenRoute(
    state: LeaderboardViewModel.UIState,
    getTopPlayersByLevel: (String) -> Unit,
    changeTab: (Int) -> Unit,
    filterChanged: (String) -> Unit,
    navigateBack: () -> Boolean
) {
    when (state) {
        is LeaderboardViewModel.UIState.Error -> {
            ErrorScreen(
                errorMessage = stringResource(state.message),
                onClick = { getTopPlayersByLevel(PlayerLevel.RECRUIT.badgeName) }
            )
        }

        is LeaderboardViewModel.UIState.Success -> {
            LeaderboardScreen(
                uiState = state.data,
                onTabSelected = changeTab,
                onFilterChanged = filterChanged,
                navigateBack = navigateBack
            )
        }

        else -> {}
    }
}

@Composable
private fun LeaderboardScreen(
    uiState: LeaderboardUiState,
    onTabSelected: (Int) -> Unit,
    onFilterChanged: (String) -> Unit,
    navigateBack: () -> Boolean
) {
    Scaffold(
        topBar = {
            TopBar(
                backgroundColor = LeaderboardBackground,
                title = stringResource(R.string.leaderboard_title),
                isBackEnabled = true,
                onBackClicked = navigateBack
            )
        },
        containerColor = LeaderboardBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    TabRow(
                        selectedTabIndex = uiState.selectedTab,
                        containerColor = LeaderboardBackground,
                        contentColor = LeaderboardAccentColor,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab]),
                                color = LeaderboardAccentColor
                            )
                        }
                    ) {
                        Tab(
                            selected = uiState.selectedTab == 0,
                            onClick = { onTabSelected(0) },
                            text = {
                                Text(
                                    style = MaterialTheme.typography.labelSmall,
                                    text = stringResource(R.string.tab_level),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        )
                        Tab(
                            selected = uiState.selectedTab == 1,
                            onClick = { onTabSelected(1) },
                            text = {
                                Text(
                                    style = MaterialTheme.typography.labelSmall,
                                    text = stringResource(R.string.tab_category),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        )
                    }

                    FilterSelector(
                        selectedTab = uiState.selectedTab,
                        currentFilter = uiState.selectedFilter,
                        onFilterSelected = onFilterChanged
                    )

                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = LeaderboardAccentColor)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(
                                bottom = 100.dp,
                                top = 8.dp
                            )
                        ) {
                            items(uiState.leaderboardList.size) { index ->
                                LeaderboardRow(
                                    item = uiState.leaderboardList[index],
                                    surfaceColor = LeaderboardSurfaceColor,
                                    accentColor = LeaderboardAccentColor
                                )
                            }
                        }
                    }
                }

                uiState.currentUserItem?.let { userItem ->
                    val isUserInTop100 =
                        uiState.leaderboardList.any { it.username == userItem.username }
                    if (!isUserInTop100) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(PurpleGrey40)
                                .padding(16.dp)
                        ) {
                            LeaderboardRow(
                                item = userItem,
                                surfaceColor = Color(0xFF0369A1),
                                accentColor = White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LeaderboardRow(
    item: LeaderboardItem,
    surfaceColor: Color,
    accentColor: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        modifier = Modifier.fillMaxWidth(),
        border = if (item.isCurrentUser) CardDefaults.outlinedCardBorder()
            .copy(width = 1.5.dp) else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val positionColor = when (item.position) {
                1 -> Gold
                2 -> Silver
                3 -> Bronze
                else -> White.copy(alpha = 0.7f)
            }

            Text(
                style = MaterialTheme.typography.labelMedium,
                text = stringResource(R.string.user_position, item.position),
                fontWeight = FontWeight.Black,
                color = positionColor,
                modifier = Modifier.width(48.dp)
            )

            Text(
                style = MaterialTheme.typography.labelSmall,
                text = if (item.isCurrentUser)
                    stringResource(id = R.string.current_user, item.username)
                else
                    item.username,
                fontWeight = if (item.isCurrentUser) FontWeight.Bold else FontWeight.Normal,
                color = White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Text(
                style = MaterialTheme.typography.labelSmall,
                text = item.scoreDetail,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        }
    }
}

@Composable
private fun FilterSelector(
    selectedTab: Int,
    currentFilter: String,
    onFilterSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val filters = if (selectedTab == 0) {
        PlayerLevel.entries.map { it.badgeName }
    } else {
        QuizCategory.entries.map { it.categoryName }
    }

    Box(modifier = Modifier.padding(16.dp)) {
        OutlinedButton(
            onClick = { expanded = true },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = White),
            border = ButtonDefaults.outlinedButtonBorder().copy(width = 1.dp)
        ) {
            Text(
                style = MaterialTheme.typography.displaySmall,
                text = stringResource(id = R.string.filter_by, currentFilter),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(LeaderboardSurfaceColor)
        ) {
            filters.forEach { filter ->
                DropdownMenuItem(
                    text = {
                        Text(
                            filter,
                            color = White,
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Start,
                        )
                    },
                    onClick = {
                        onFilterSelected(filter)
                        expanded = false
                    }
                )
            }
        }
    }
}