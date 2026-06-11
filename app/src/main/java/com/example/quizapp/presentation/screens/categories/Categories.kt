package com.example.quizapp.presentation.screens.categories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizapp.R
import com.example.quizapp.presentation.components.BottomNavigationBar
import com.example.quizapp.presentation.isSplashScreenOpen
import com.example.quizapp.presentation.navigation.Destination
import com.example.quizapp.presentation.core.Black
import com.example.quizapp.presentation.core.PurpleGrey40
import com.example.quizapp.presentation.core.White

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
    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .safeDrawingPadding()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 20.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(20.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories.size) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(width = 2.dp, color = White),
                        colors = CardDefaults.cardColors(
                            containerColor = White,
                            contentColor = Black,
                            disabledContainerColor = White,
                            disabledContentColor = Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable { navigateToCategory(it) }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = categoriesImages[it]),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(0.5f),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = stringResource(id = categories[it]),
                                color = Black,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}