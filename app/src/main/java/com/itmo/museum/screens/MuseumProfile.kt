package com.itmo.museum.screens

import android.location.Location
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.itmo.museum.R
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
    onAddReviewClick: (isReviewed: Boolean, museumId: Int) -> Unit = { _, _ -> },
) {
    val uiState by viewModel.uiState.collectAsState()

    viewModel.checkIfReviewed()
    val isReviewed by viewModel.isReviewed.collectAsState()
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
                    modifier = Modifier.testTag(stringResource(id = R.string.mark_as_visited_button)),
                    onClick = { onVisitedClick(uiState.museumDetails.toMuseum()) }
                ) {
                    Text(text = "Mark as visited")
                }
                MuseumInfo(museum = uiState.museumDetails)
                ReviewList(
                    viewModel = viewModel,
                    onAddReviewClick = {
                        onAddReviewClick(
                            isReviewed == IsReviewedState.IS_REVIEWED,
                            uiState.museumDetails.id
                        )
                    },
                    reviews = uiState.reviews
                )
                WideButton(
                    enabled = true,
                    text = "Route",
                    onClick = {
                        mapState.lastKnownLocation
                            ?.let { userLocation ->
                                onRouteClicked(
                                    userLocation,
                                    uiState.museumDetails
                                )
                            }
                            ?: makeLocationNotFoundToast(context)
                    }
                )
            }
        }
    }
}
