package com.apollo9921.quizrise.presentation.screens.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.usecase.FetchBadgeImageUseCase
import com.apollo9921.quizrise.domain.usecase.FetchUserUseCase
import com.apollo9921.quizrise.domain.usecase.FetchBadgeUseCase
import com.apollo9921.quizrise.domain.usecase.GetMyCategoryRankUseCase
import com.apollo9921.quizrise.domain.usecase.GetUserLevelRankUseCase
import com.apollo9921.quizrise.domain.util.NetworkObserver
import com.apollo9921.quizrise.domain.util.PlayerLevel
import com.apollo9921.quizrise.presentation.components.Achievement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val fetchUserUseCase: FetchUserUseCase,
    private val fetchBadgeImageUseCase: FetchBadgeImageUseCase,
    private val fetchBadgeUseCase: FetchBadgeUseCase,
    private val getUserLevelRankUseCase: GetUserLevelRankUseCase,
    private val getMyCategoryRankUseCase: GetMyCategoryRankUseCase,
    private val networkObserver: NetworkObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _badgeState = MutableStateFlow<Badge>(Badge())
    val badgeState = _badgeState.asStateFlow()

    private val _rankState = MutableStateFlow<Int?>(null)
    val rankState = _rankState.asStateFlow()

    private var _categoriesRankState = MutableStateFlow<List<Achievement>>(emptyList())
    val categoriesRankState = _categoriesRankState.asStateFlow()

    data class Badge(
        var badge: Int = 0,
        var badgeLevel: Int = 0
    )

    sealed class UIState {
        data object Idle : UIState()
        data class Success(val user: User) : UIState()
    }

    init {
        observeNetworkConnectivity()
    }

    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkObserver.isConnected.collect { isOnline ->
                val currentState = _uiState.value
                if (isOnline && currentState is UIState.Success) {
                    getUserRank(currentState.user)
                    getCategoriesRank()
                }
            }
        }
    }

    fun fetchUser(context: Context) {
        viewModelScope.launch {
            val result = fetchUserUseCase.invoke()
            if (result.isSuccess) {
                var data = result.getOrThrow()
                val badgeSymbol = fetchBadgeImageUseCase.invoke(data)
                val badgeMaxPoints = fetchBadgeUseCase.invoke(data)

                val badgeName = PlayerLevel.getLevelByPoints(data.totalPoints).resourceId
                data = data.copy(badge = context.getString(badgeName))

                _badgeState.value = Badge(badgeSymbol, badgeMaxPoints)
                _uiState.value = UIState.Success(user = data)
            }
        }
    }

    private fun getUserRank(data: User) {
        viewModelScope.launch {
            val badge = PlayerLevel.getLevelByPoints(data.totalPoints).badgeName
            getUserLevelRankUseCase.invoke(
                badge = badge,
                totalPoints = data.totalPoints,
                onSuccess = { rank ->
                    _rankState.value = rank
                },
                onFailure = { _ ->
                    if (rankState.value == null) {
                        _rankState.value = null
                    } else {
                        _rankState.value = rankState.value
                    }
                }
            )
        }
    }

    private fun getCategoriesRank() {
        viewModelScope.launch {
            _categoriesRankState.value = ArrayList()
            getMyCategoryRankUseCase.invoke(
                onSuccess = { achievement ->
                    if (achievement != null) {
                        _categoriesRankState.value += achievement
                    }
                },
                onFailure = { _ ->
                    if (categoriesRankState.value.isEmpty()) {
                        _categoriesRankState.value = ArrayList()
                    } else {
                        _categoriesRankState.value = categoriesRankState.value
                    }
                }
            )
        }
    }
}