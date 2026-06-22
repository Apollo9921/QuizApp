package com.example.quizapp.presentation.screens.categories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.presentation.components.BottomNavigationBar
import com.example.quizapp.presentation.isSplashScreenOpen
import com.example.quizapp.presentation.navigation.Destination
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.White
import com.example.quizapp.presentation.utils.componentSizeByScreen
import com.example.quizapp.presentation.utils.widthOfScreen

@Composable
fun CategoriesRoute(navHostController: NavHostController) {
    val categories = listOf(
        R.string.artsAndLiterature_translatable,
        R.string.filmAndTV_translatable,
        R.string.foodAndDrink_translatable,
        R.string.generalKnowledge_translatable,
        R.string.geography_translatable,
        R.string.history_translatable,
        R.string.music_translatable,
        R.string.science_translatable,
        R.string.societyAndCulture_translatable,
        R.string.sportAndLeisure_translatable
    )
    isSplashScreenOpen = false
    val context = LocalContext.current
    val navigateToCategory = { it: Int ->
        navHostController.navigate(
            Destination.LevelDifficulty.passArgument(
                context.resources.getString(
                    categories[it]
                )
            )
        )
    }
    val categoriesImages = listOf(
        R.drawable.book,
        R.drawable.movie,
        R.drawable.food,
        R.drawable.knowledge,
        R.drawable.geography,
        R.drawable.history,
        R.drawable.music,
        R.drawable.science,
        R.drawable.society,
        R.drawable.sports
    )

    CategoriesScreen(
        navHostController = navHostController,
        navigateToCategory = navigateToCategory,
        categoriesImages = categoriesImages,
        categories = categories
    )
}

@Composable
private fun CategoriesScreen(
    navHostController: NavHostController,
    navigateToCategory: (Int) -> Unit,
    categoriesImages: List<Int>,
    categories: List<Int>
) {
    val screenWidth = widthOfScreen()

    val columnCount = when {
        screenWidth < 600.dp -> 2
        screenWidth < 840.dp -> 3
        else -> 4
    }

    val maxLayoutWidth = if (screenWidth < 600.dp) Dp.Unspecified else componentSizeByScreen(720.dp)

    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController) },
        containerColor = PurpleGrey40
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(bottom = paddingValues.calculateBottomPadding()),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columnCount),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = maxLayoutWidth)
                    .fillMaxWidth()
            ) {
                items(categories.size) { index ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(width = 1.dp, color = White.copy(alpha = 0.15f)),
                        colors = CardDefaults.cardColors(
                            containerColor = White.copy(alpha = 0.06f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { navigateToCategory(index) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = categoriesImages[index]),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(0.65f),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = stringResource(id = categories[index]),
                                color = White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}