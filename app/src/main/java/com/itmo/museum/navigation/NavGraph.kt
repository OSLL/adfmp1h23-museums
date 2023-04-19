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
import com.itmo.museum.screens.AboutScreen
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
    fun onAboutClicked() {
        navController.navigate(MuseumAppScreen.About.route)
    }
    
    fun onBackClicked() {
        navController.navigateUp()
    }
    
    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        startDestination = MuseumAppScreen.BottomBarScreen.Museums.route
    ) {
        composable(route = MuseumAppScreen.BottomBarScreen.Museums.route) {
            MuseumsScreen(
                onBackClicked = ::onBackClicked,
                onAboutClicked = ::onAboutClicked,
                onMuseumClicked = { museum -> navController.navigate(museum) }
            )
        }
        composable(route = MuseumAppScreen.BottomBarScreen.Visited.route) {
            VisitedScreen(
                onBackClicked = ::onBackClicked,
                onAboutClicked = ::onAboutClicked,
                viewModel = viewModel
            )
        }
        composable(route = MuseumAppScreen.About.route) {
            AboutScreen(
                onBackClicked = ::onBackClicked,
            )
        }
        MuseumDataProvider.defaultProvider.museums.forEach { museum ->
            composable(route = MuseumAppScreen.MuseumProfile(museum).route) {
                MuseumProfile(
                    museum = museum,
                    onBackClicked = ::onBackClicked,
                    onAboutClicked = ::onAboutClicked,
                    onRouteClicked = { navController.navigate(museum.routePage) }
                )
            }
            composable(route = MuseumAppScreen.Route(museum).route) {
                RouteScreen(
                    onBackClicked = ::onBackClicked,
                    onAboutClicked = ::onAboutClicked,
                    targetMuseum = museum
                )
            }
        }
    }
}
