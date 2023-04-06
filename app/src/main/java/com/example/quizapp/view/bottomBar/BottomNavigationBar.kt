package com.example.quizapp.view.bottomBar

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.quizapp.view.theme.Black
import com.example.quizapp.view.theme.Purple40
import com.example.quizapp.view.theme.White

private var selectedItem = mutableStateOf(BottomNavigationItems.Progress.route)

@Composable
fun bottomNavigationBar(): String {
    val items = listOf(
        BottomNavigationItems.Progress,
        BottomNavigationItems.Quiz,
        BottomNavigationItems.Results,
        BottomNavigationItems.Profile
    )

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
                        painterResource(id = item.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = LocalContext.current.getString(item.title)
                    )
                },
                selected = selectedItem.value == items[index].route,
                onClick = {
                    selectedItem.value = items[index].route
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
    return selectedItem.value
}