package com.apollo9921.quizrise.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.apollo9921.quizrise.presentation.navigation.BottomNavigationItems
import com.apollo9921.quizrise.presentation.core.Black
import com.apollo9921.quizrise.presentation.core.Purple40
import com.apollo9921.quizrise.presentation.core.White
import com.apollo9921.quizrise.presentation.utils.componentSizeByScreen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavigationItems.Quiz,
        BottomNavigationItems.Progress,
        BottomNavigationItems.Results,
        BottomNavigationItems.Profile
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val cornerRadius = componentSizeByScreen(baseSize = 40.dp)
    val iconSize = componentSizeByScreen(baseSize = 24.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        NavigationBar(
            containerColor = Purple40,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(
                    RoundedCornerShape(
                        topStart = cornerRadius,
                        topEnd = cornerRadius
                    )
                )
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = currentRoute == items[index].route

                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = null,
                            modifier = Modifier.size(iconSize)
                        )
                    },
                    label = {
                        Text(
                            style = MaterialTheme.typography.displaySmall,
                            text = stringResource(item.title)
                        )
                    },
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Black,
                        selectedTextColor = White,
                        unselectedIconColor = White,
                        unselectedTextColor = White
                    )
                )
            }
        }
    }
}