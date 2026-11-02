package com.apollo9921.quizrise.presentation.screens.achievements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.util.QuizCategory
import com.apollo9921.quizrise.presentation.components.TopBar
import com.apollo9921.quizrise.presentation.core.QuizAppTheme
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen

data class AchievementDetail(
    val title: String,
    val iconRes: Int,
    val isUnlocked: Boolean
)

@Composable
fun AchievementsRoute(
    navHostController: NavHostController,
    achievementsTitle: List<String?>,
    achievementsIcon: List<Int?>
) {
    val allCategories = QuizCategory.getAllCategories()

    val achievementsList = allCategories.map { category ->
        val title = achievementsTitle.find { it == category.categoryName }
        val iconRes = achievementsIcon.find { it == QuizCategory.getBadgeDrawableTop1ForCategory(category.categoryName) } ?: 0
        if (title != null) {
            AchievementDetail(title, iconRes, true)
        } else {
            AchievementDetail(
                category.categoryName,
                QuizCategory.getBadgeDrawableTop1ForCategory(category.categoryName),
                false
            )
        }
    }

    AchievementsScreen(
        onBackClick = { navHostController.navigateUp() },
        achievements = achievementsList
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AchievementsScreen(
    onBackClick: () -> Unit,
    achievements: List<AchievementDetail>
) {
    Scaffold(
        topBar = {
            TopBar(
                backgroundColor = MaterialTheme.colorScheme.primary,
                title = stringResource(R.string.achievements),
                onClick = { onBackClick() }
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item(span = { GridItemSpan(3) }) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "TOP 1",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.top1_each_category),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                items(achievements) { achievement ->
                    DetailedAchievementItem(achievement = achievement)
                }
            }
        }
    }
}

@Composable
private fun DetailedAchievementItem(achievement: AchievementDetail) {
    val alphaVal = if (achievement.isUnlocked) 1f else 0.35f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alphaVal)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(componentSizeByScreen(90.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(
                        alpha = if (achievement.isUnlocked) 0.12f else 0.04f
                    )
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surface.copy(
                        alpha = if (achievement.isUnlocked) 0.3f else 0.1f
                    ),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = achievement.iconRes),
                contentDescription = stringResource(QuizCategory.getResourceByName(achievement.title)),
                modifier = Modifier.size(componentSizeByScreen(90.dp)),
                colorFilter = if (!achievement.isUnlocked) {
                    ColorFilter.tint(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                } else null
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(QuizCategory.getResourceByName(achievement.title)),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.surface,
            fontWeight = if (achievement.isUnlocked) FontWeight.Bold else FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AchievementsScreenPreview() {
    QuizAppTheme {
        AchievementsScreen(
            onBackClick = {},
            achievements = listOf(
                AchievementDetail(QuizCategory.ARTS_AND_LITERATURE.categoryName, R.drawable.badge_arts_and_literature_top1, true),
                AchievementDetail(QuizCategory.FILM_AND_TV.categoryName, R.drawable.badge_film_tv_top1, true),
                AchievementDetail(QuizCategory.FOOD_AND_DRINK.categoryName, R.drawable.badge_food_drink_top1, false),
                AchievementDetail(QuizCategory.GENERAL_KNOWLEDGE.categoryName, R.drawable.badge_general_knowledge_top1, true),
                AchievementDetail(QuizCategory.GEOGRAPHY.categoryName, R.drawable.badge_geography_top1, false),
                AchievementDetail(QuizCategory.HISTORY.categoryName, R.drawable.badge_history_top1, false),
                AchievementDetail(QuizCategory.MUSIC.categoryName, R.drawable.badge_music_top1, true),
                AchievementDetail(QuizCategory.SCIENCE.categoryName, R.drawable.badge_science_top1, false),
                AchievementDetail(QuizCategory.SOCIETY_AND_CULTURE.categoryName, R.drawable.badge_society_culture_top1, false),
                AchievementDetail(QuizCategory.SPORT_AND_LEISURE.categoryName, R.drawable.badge_sport_leisure_top1, false)
            )
        )
    }
}