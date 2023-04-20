package com.itmo.museum.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.itmo.museum.elements.*
import com.itmo.museum.elements.defaultMuseum
import com.itmo.museum.models.Museum
import com.itmo.museum.ui.theme.MuseumTheme

@Composable
fun MuseumProfile(
    navController: NavHostController,
    museum: Museum = defaultMuseum,
    onBackClicked: () -> Unit = {},
    onRouteClicked: () -> Unit = {},
    onVisitedClick: (Museum) -> Unit = {}
) {
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
                MuseumCard(museum = museum)
                Button(
                    onClick = { onVisitedClick(museum) }
                ) {
                    Text(text = "Mark as visited")
                }
                MuseumInfo(museum = museum)
                ReviewList(museum.reviews)
                RouteButton(onClick = onRouteClicked)
            }
        }
    }
}
