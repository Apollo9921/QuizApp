package com.example.quizapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.quizapp.view.bottomBar.BottomNavigationItems
import com.example.quizapp.view.bottomBar.bottomNavigationBar
import com.example.quizapp.view.theme.PurpleGrey40

private var index = mutableStateOf("")

@Composable
fun Main(navHostController: NavHostController) {
    Scaffold(
        bottomBar = { index.value = bottomNavigationBar() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey40)
                .padding(bottom = it.calculateBottomPadding())
        ){
            when(index.value) {
                BottomNavigationItems.Progress.route -> {
                    Progress()
                }
                BottomNavigationItems.Quiz.route -> {
                    Quiz(navHostController = navHostController)
                }
                BottomNavigationItems.Results.route -> {
                    Results()
                }
                BottomNavigationItems.Profile.route -> {
                    UserProfile()
                }
            }
        }
    }
}