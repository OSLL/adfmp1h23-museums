package com.itmo.museum.screens

import android.location.Location
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.itmo.museum.elements.*
import com.itmo.museum.models.*
import com.itmo.museum.ui.theme.MuseumTheme
import com.itmo.museum.util.makeLocationNotFoundToast
import com.itmo.museum.util.toMuseum

@Composable
fun MuseumProfile(
    navController: NavHostController,
    mapViewModel: MapViewModel,
    viewModel: MuseumProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackClicked: () -> Unit = {},
    onRouteClicked: (source: Location, destination: MuseumDetails) -> Unit = { _, _ -> },
    onVisitedClick: (Museum) -> Unit = {},
    onAddReviewClick: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val mapState by mapViewModel.uiState.collectAsState()

    MuseumTheme {
        Scaffold(
            topBar = {
                MuseumAppTopBar(
                    titleText = uiState.museumDetails.name,
                    onBackClicked = onBackClicked,
                )
            },
            bottomBar = { BottomBar(navController = navController) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(state = ScrollState(0)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MuseumCard()
                Button(
                    onClick = { onVisitedClick(uiState.museumDetails.toMuseum()) }
                ) {
                    Text(text = "Mark as visited")
                }
                MuseumInfo(museum = uiState.museumDetails)
                ReviewList(
                    onAddReviewClick = { onAddReviewClick(uiState.museumDetails.id) },
                    reviews = uiState.reviews
                )
                WideButton(
                    text = "Route",
                    onClick = {
                        mapState.lastKnownLocation
                            ?.let { userLocation -> onRouteClicked(userLocation, uiState.museumDetails) }
                            ?: makeLocationNotFoundToast(context)
                    }
                )
            }
        }
    }
}
