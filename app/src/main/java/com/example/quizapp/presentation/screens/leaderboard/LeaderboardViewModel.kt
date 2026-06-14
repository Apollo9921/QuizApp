package com.example.quizapp.presentation.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.usecase.GetTopPlayersByCategoryUseCase
import com.example.quizapp.domain.usecase.GetTopPlayersByLevelUseCase
import com.example.quizapp.domain.util.PlayerLevel
import com.example.quizapp.domain.util.QuizCategory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val getTopPlayersByLevelUseCase: GetTopPlayersByLevelUseCase,
    private val getTopPlayersByCategoryUseCase: GetTopPlayersByCategoryUseCase
) : ViewModel() {

    private val currentUserId: String
        get() = firebaseAuth.currentUser?.uid ?: ""

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    data class LeaderboardItem(
        val position: Int,
        val username: String,
        val scoreDetail: String,
        val isCurrentUser: Boolean = false
    )

    data class LeaderboardUiState(
        val selectedTab: Int = 0,
        val selectedFilter: String = PlayerLevel.RECRUIT.badgeName,
        val leaderboardList: List<LeaderboardItem> = emptyList(),
        val currentUserItem: LeaderboardItem? = null,
        val isLoading: Boolean = false
    )

    sealed class UIState {
        data object Idle : UIState()
        data class Success(val data: LeaderboardUiState) : UIState()
        data class Error(val message: String) : UIState()
    }

    init {
        _uiState.value = UIState.Success(data = LeaderboardUiState(isLoading = true))
        getTopPlayersByLevel(selectedBadge = PlayerLevel.RECRUIT.badgeName)
    }

    fun changeTab(tab: Int) {
        if (tab == 0) {
            getTopPlayersByLevel(selectedBadge = PlayerLevel.RECRUIT.badgeName)
        } else if (tab == 1) {
            getTopPlayersByCategory(selectedCategory = QuizCategory.ARTS_AND_LITERATURE.categoryName)
        }
    }

    fun changeFilter(filter: String) {
        val previousState = _uiState.value as UIState.Success
        if (previousState.data.selectedTab == 0) {
            getTopPlayersByLevel(selectedBadge = filter)
        } else if (previousState.data.selectedTab == 1) {
            getTopPlayersByCategory(selectedCategory = filter)
        }
    }

    fun getTopPlayersByLevel(selectedBadge: String) {
        viewModelScope.launch {
            _uiState.value = UIState.Success(
                data = LeaderboardUiState(
                    selectedFilter = selectedBadge,
                    selectedTab = 0,
                    isLoading = true
                )
            )
            getTopPlayersByLevelUseCase.invoke(
                badge = selectedBadge,
                onSuccess = {
                    val items = it.mapIndexed { index, item ->
                        LeaderboardItem(
                            position = index + 1,
                            username = item.name,
                            scoreDetail = "${item.totalPoints} pts",
                            isCurrentUser = item.id == currentUserId
                        )
                    }
                    _uiState.value = UIState.Success(
                        data = LeaderboardUiState(
                            selectedFilter = selectedBadge,
                            selectedTab = 0,
                            leaderboardList = items,
                            isLoading = false
                        )
                    )
                },
                onFailure = {
                    _uiState.value =
                        UIState.Error(message = it.message ?: "Error")
                })
        }
    }

    fun getTopPlayersByCategory(selectedCategory: String) {
        viewModelScope.launch {
            _uiState.value = UIState.Success(
                data = LeaderboardUiState(
                    selectedFilter = selectedCategory,
                    selectedTab = 1,
                    isLoading = true
                )
            )
            getTopPlayersByCategoryUseCase.invoke(
                category = selectedCategory,
                onSuccess = {
                    val items = it.mapIndexed { index, item ->
                        LeaderboardItem(
                            position = index + 1,
                            username = item.username,
                            scoreDetail = "${item.correctAnswers * 5} pts",
                            isCurrentUser = item.userId == currentUserId
                        )
                    }
                    _uiState.value = UIState.Success(
                        data = LeaderboardUiState(
                            selectedFilter = selectedCategory,
                            selectedTab = 1,
                            leaderboardList = items,
                            isLoading = false
                        )
                    )
                },
                onFailure = {
                    _uiState.value = UIState.Error(message = it.message ?: "Error")
                })
        }
    }
}