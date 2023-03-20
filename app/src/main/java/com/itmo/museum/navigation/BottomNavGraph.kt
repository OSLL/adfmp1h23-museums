package com.itmo.museum.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itmo.museum.screens.MuseumsScreen
import com.itmo.museum.screens.VisitedScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Museums.route
    ) {
        composable(route = BottomBarScreen.Museums.route) {
            MuseumsScreen()
        }
        composable(route = BottomBarScreen.Visited.route) {
            VisitedScreen()
        }
    }
}
