package com.example.quizapp.view.bottomBar

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.quizapp.view.theme.Black
import com.example.quizapp.view.theme.Purple40
import com.example.quizapp.view.theme.White

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

    NavigationBar(
        containerColor = Purple40,
        modifier = Modifier
            .graphicsLayer {
                shape = RoundedCornerShape(
                    topStart = 50.dp,
                    topEnd = 50.dp
                )
                clip = true
            }
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        style = MaterialTheme.typography.labelSmall,
                        text = stringResource(item.title)
                    )
                },
                selected = currentRoute == items[index].route,
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