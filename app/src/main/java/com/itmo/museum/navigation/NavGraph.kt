package com.itmo.museum.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.itmo.museum.R
import com.itmo.museum.elements.AddReview
import com.itmo.museum.elements.MuseumSearchUI
import com.itmo.museum.models.*
import com.itmo.museum.screens.AboutScreen
import com.itmo.museum.screens.GreetingScreen
import com.itmo.museum.screens.MuseumProfile
import com.itmo.museum.screens.VisitedScreen
import com.itmo.museum.util.drawRoute

@ExperimentalComposeUiApi
@Composable
fun NavGraph(
    navController: NavHostController,
    mapViewModel: MapViewModel,
    viewModel: MuseumListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    fun onBackClick() {
        navController.navigateUp()
    }

    val startDestination = uiState.user
        ?.let { MuseumAppScreen.BottomBarScreen.Museums }
        ?: MuseumAppScreen.Greeting

    val context = LocalContext.current

    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(route = MuseumAppScreen.Greeting.route) {
            GreetingScreen(
                onLoginClick = { username ->
                    context.rememberUsername(username)
                    viewModel.addUser(User(name = username))
                    navController.navigate(MuseumAppScreen.BottomBarScreen.Museums.route)
                },
                onSkipLoginClick = {
                    context.rememberAnonymousUser()
                    viewModel.addUser(User(name = UserDetails.Anonymous.name))
                    navController.navigate(MuseumAppScreen.BottomBarScreen.Museums.route)
                }
            )
        }

        composable(route = MuseumAppScreen.BottomBarScreen.Museums.route) {
            MuseumSearchUI(
                navController = navController,
                onBackClick = ::onBackClick,
                onMuseumClick = { museumId ->
                    navController.navigate("${MuseumAppScreen.WithArgs.MuseumProfile.route}/$museumId")
                }
            )
        }
        composable(route = MuseumAppScreen.BottomBarScreen.Visited.route) {
            VisitedScreen(
                navController = navController,
                onBackClicked = ::onBackClick,
                viewModel = viewModel,
                onMuseumClick = { museumId ->
                    navController.navigate("${MuseumAppScreen.WithArgs.MuseumProfile.route}/$museumId")
                }
            )
        }
        composable(route = MuseumAppScreen.BottomBarScreen.About.route) {
            AboutScreen(
                navController = navController,
                onBackClicked = ::onBackClick,
            )
        }
        composable(
            route = MuseumAppScreen.WithArgs.MuseumProfile.routeWithArgs,
            arguments = listOf(navArgument(MuseumAppScreen.WithArgs.MuseumProfile.museumIdArg) {
                type = NavType.IntType
            })
        ) {
            MuseumProfile(
                navController = navController,
                mapViewModel = mapViewModel,
                onBackClicked = ::onBackClick,
                onRouteClicked = { source, destination ->
                    drawRoute(source, destination, context)
                },
                onVisitedClick = { museum -> viewModel.markAsVisited(museum) },
                onAddReviewClick = {
                    navController.navigate("${MuseumAppScreen.WithArgs.AddReview.route}/$it")
                },
            )
        }
        composable(
            route = MuseumAppScreen.WithArgs.AddReview.routeWithArgs,
            arguments = listOf(navArgument(MuseumAppScreen.WithArgs.AddReview.museumIdArg) {
                type = NavType.IntType
            })
        ) {
            AddReview(
                navController = navController,
                onBackClicked = ::onBackClick
            )
        }
    }
}

private fun Context.rememberUsername(username: String) {
    val preferences =
        getSharedPreferences(getString(R.string.user_preferences_file_key), Context.MODE_PRIVATE)
    with(preferences.edit()) {
        putString(getString(R.string.username_key), username)
    }
}

private fun Context.rememberAnonymousUser() {
    rememberUsername(UserDetails.Anonymous.name)
}
