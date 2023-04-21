package com.itmo.museum.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.itmo.museum.elements.MuseumAppTopBar
import com.itmo.museum.models.AppViewModel
import com.itmo.museum.models.Museum

@Composable
fun VisitedScreen(
    navController: NavHostController,
    onBackClicked: () -> Unit = {},
    onMuseumClick: (String) -> Unit = {},
    viewModel: AppViewModel
) {
    Scaffold(
        topBar = {
            MuseumAppTopBar(
                titleText = "Visited",
                onBackClicked = onBackClicked,
            )
        },
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        VisitedScreenContent(
            onMuseumClick = onMuseumClick,
            viewModel = viewModel,
            padding = innerPadding
        )
    }
}

@Composable
private fun VisitedScreenContent(
    onMuseumClick: (String) -> Unit = {},
    viewModel: AppViewModel,
    padding: PaddingValues
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        val visitedMuseums = uiState.visitedMuseums.toList()
        if (visitedMuseums.isEmpty()) {
            NoMuseumsVisited()
        } else {
            VisitedMuseumsList(
                viewModel = viewModel,
                visitedMuseums = visitedMuseums,
                onMuseumClick = onMuseumClick
            )
        }
    }
}

@Composable
private fun NoMuseumsVisited() {
    Text(
        text = "No museums visited yet",
        fontSize = MaterialTheme.typography.h4.fontSize,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
}

@Composable
private fun VisitedMuseumsList(
    viewModel: AppViewModel,
    visitedMuseums: List<Museum>,
    onMuseumClick: (String) -> Unit = {}
) {
    Box {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            MuseumCardList(
                viewModel = viewModel,
                onMuseumClick = onMuseumClick,
                backgroundColor = Color.Blue,
                museums = visitedMuseums
            )
        }
    }
}
