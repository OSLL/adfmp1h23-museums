package com.itmo.museum.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itmo.museum.data.MuseumDataProvider
import com.itmo.museum.elements.MuseumSearchUI
import com.itmo.museum.elements.RouteScreen
import com.itmo.museum.models.AppViewModel
import com.itmo.museum.models.MuseumSearchViewModel
import com.itmo.museum.screens.AboutScreen
import com.itmo.museum.screens.MuseumProfile
import com.itmo.museum.screens.VisitedScreen
import com.itmo.museum.util.routePage

@ExperimentalComposeUiApi
@Composable
fun NavGraph(
    innerPadding: PaddingValues,
    navController: NavHostController,
    viewModel: AppViewModel = viewModel()
) {
    fun onBackClick() {
        navController.navigateUp()
    }

    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        startDestination = MuseumAppScreen.BottomBarScreen.Museums.route
    ) {
        composable(route = MuseumAppScreen.BottomBarScreen.Museums.route) {
            val museumSearchViewModel = hiltViewModel<MuseumSearchViewModel>()
            MuseumSearchUI(
                museumSearchViewModel = museumSearchViewModel,
                onSearchTextChanged = { museumSearchViewModel.onSearchTextChanged(it) },
                onBackClick = ::onBackClick,
                onClearClick = { museumSearchViewModel.onClearClick() },
                onMuseumClick = { museum -> navController.navigate(museum) }
            )
        }
        composable(route = MuseumAppScreen.BottomBarScreen.Visited.route) {
            VisitedScreen(
                onBackClicked = ::onBackClick,
                viewModel = viewModel,
                onMuseumClick = { museum -> navController.navigate(museum) }
            )
        }
        composable(route = MuseumAppScreen.BottomBarScreen.About.route) {
            AboutScreen(
                onBackClicked = ::onBackClick,
            )
        }
        MuseumDataProvider.defaultProvider.museums.forEach { museum ->
            composable(route = MuseumAppScreen.MuseumProfile(museum).route) {
                MuseumProfile(
                    museum = museum,
                    onBackClicked = ::onBackClick,
                    onRouteClicked = { navController.navigate(museum.routePage) },
                    onVisitedClick = { museum -> viewModel.addVisitedMuseum(museum) }
                )
            }
            composable(route = MuseumAppScreen.Route(museum).route) {
                RouteScreen(
                    onBackClicked = ::onBackClick,
                    targetMuseum = museum
                )
            }
        }
    }
}
