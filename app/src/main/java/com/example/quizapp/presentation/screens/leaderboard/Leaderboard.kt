package com.example.quizapp.presentation.screens.leaderboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.example.quizapp.domain.util.PlayerLevel
import com.example.quizapp.presentation.components.ErrorScreen
import com.example.quizapp.presentation.components.LeaderboardScreen
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
                errorMessage = state.message,
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