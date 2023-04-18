package com.itmo.museum.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itmo.museum.data.MuseumDataProvider
import com.itmo.museum.elements.RouteScreen
import com.itmo.museum.models.AppViewModel
import com.itmo.museum.screens.MuseumProfile
import com.itmo.museum.screens.MuseumsScreen
import com.itmo.museum.screens.VisitedScreen
import com.itmo.museum.util.routePage

@Composable
fun NavGraph(
    innerPadding: PaddingValues,
    navController: NavHostController,
    viewModel: AppViewModel = viewModel()
) {
    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        startDestination = BottomBarScreen.Museums.route
    ) {
        composable(route = BottomBarScreen.Museums.route) {
            MuseumsScreen(
                onMuseumClicked = { museum -> navController.navigate(museum) }
            )
        }
        composable(route = BottomBarScreen.Visited.route) {
            VisitedScreen(viewModel)
        }
        MuseumDataProvider.defaultProvider.museums.forEach { museum ->
            composable(route = museum.name) {
                MuseumProfile(
                    museum = museum,
                    onRouteClicked = { navController.navigate(museum.routePage) }
                )
            }
            composable(route = museum.routePage) {
                RouteScreen(targetMuseum = museum)
            }
        }
    }
}
