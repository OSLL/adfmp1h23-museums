package com.itmo.museum.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itmo.museum.R
import com.itmo.museum.elements.AddReview
import com.itmo.museum.elements.MuseumSearchUI
import com.itmo.museum.models.AppViewModel
import com.itmo.museum.models.MuseumSearchViewModel
import com.itmo.museum.models.User
import com.itmo.museum.models.UserReview
import com.itmo.museum.screens.AboutScreen
import com.itmo.museum.screens.GreetingScreen
import com.itmo.museum.screens.MuseumProfile
import com.itmo.museum.screens.VisitedScreen
import com.itmo.museum.util.drawRoute

@ExperimentalComposeUiApi
@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: AppViewModel = viewModel()
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
                    viewModel.setUser(
                        User(
                            name = username,
                            profilePictureId = R.drawable.default_user
                        )
                    )
                    navController.navigate(MuseumAppScreen.BottomBarScreen.Museums.route)
                },
                onSkipLoginClick = {
                    viewModel.setAnonymousUser()
                    navController.navigate(MuseumAppScreen.BottomBarScreen.Museums.route)
                }
            )
        }

        composable(route = MuseumAppScreen.BottomBarScreen.Museums.route) {
            val museumSearchViewModel = hiltViewModel<MuseumSearchViewModel>()
            MuseumSearchUI(
                navController = navController,
                viewModel = viewModel,
                museumSearchViewModel = museumSearchViewModel,
                onSearchTextChanged = { museumSearchViewModel.onSearchTextChanged(it) },
                onBackClick = ::onBackClick,
                onClearClick = { museumSearchViewModel.onClearClick() },
                onMuseumClick = { museum -> navController.navigate(museum) }
            )
        }
        composable(route = MuseumAppScreen.BottomBarScreen.Visited.route) {
            VisitedScreen(
                navController = navController,
                onBackClicked = ::onBackClick,
                viewModel = viewModel,
                onMuseumClick = { museum -> navController.navigate(museum) }
            )
        }
        composable(route = MuseumAppScreen.BottomBarScreen.About.route) {
            AboutScreen(
                navController = navController,
                onBackClicked = ::onBackClick,
            )
        }
        uiState.museums.forEach { museum ->
            composable(route = MuseumAppScreen.MuseumProfile(museum).route) {
                MuseumProfile(
                    navController = navController,
                    viewModel = viewModel,
                    museum = museum,
                    onBackClicked = ::onBackClick,
                    onRouteClicked = { source, destination ->
                        drawRoute(source, destination, context)
                    },
                    onVisitedClick = { museum -> viewModel.addVisitedMuseum(museum) },
                    onAddReviewClick = { navController.navigate(MuseumAppScreen.AddReview(museum).route) },
                )
            }
            composable(route = MuseumAppScreen.AddReview(museum).route) {
                AddReview(
                    navController = navController,
                    onBackClicked = ::onBackClick,
                    onPostReview = { rating, reviewText ->
                        val review = UserReview(
                            user = uiState.user ?: User.Anonymous,
                            rating = rating,
                            text = reviewText
                        )
                        viewModel.addReviewFor(museum, review)
                        onBackClick()
                    }
                )
            }
        }
    }
}
