package com.itmo.museum.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.itmo.museum.elements.*
import com.itmo.museum.models.AppViewModel
import com.itmo.museum.models.Museum
import com.itmo.museum.ui.theme.MuseumTheme
import com.itmo.museum.util.getReviewsFor

@Composable
fun MuseumProfile(
    navController: NavHostController,
    viewModel: AppViewModel,
    museum: Museum,
    onBackClicked: () -> Unit = {},
    onRouteClicked: (source: LatLng, destination: Museum) -> Unit = { _, _ -> },
    onVisitedClick: (Museum) -> Unit = {},
    onAddReviewClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    MuseumTheme {
        Scaffold(
            topBar = {
                MuseumAppTopBar(
                    titleText = museum.name,
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
                MuseumCard(
                    viewModel = viewModel,
                    museum = museum
                )
                Button(
                    onClick = { onVisitedClick(museum) }
                ) {
                    Text(text = "Mark as visited")
                }
                MuseumInfo(museum = museum)
                ReviewList(
                    onAddReviewClick = onAddReviewClick,
                    reviews = uiState.getReviewsFor(museum)
                )
                WideButton(
                    text = "Route",
                    onClick = {
                        val userLocation = uiState.lastKnownLocation.let {
                            LatLng(it.latitude, it.longitude)
                        }
                        onRouteClicked(userLocation, museum)
                    }
                )
            }
        }
    }
}
