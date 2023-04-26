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
import com.itmo.museum.models.Museum
import com.itmo.museum.models.MuseumListViewModel

@Composable
fun VisitedScreen(
    navController: NavHostController,
    onBackClicked: () -> Unit = {},
    onMuseumClick: (Int) -> Unit = {},
    viewModel: MuseumListViewModel
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
    onMuseumClick: (Int) -> Unit = {},
    viewModel: MuseumListViewModel,
    padding: PaddingValues
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        val visitedMuseums = uiState.visitedMuseums.toList()
        if (visitedMuseums.isEmpty()) {
            NoMuseumsVisited()
        } else {
            VisitedMuseumsList(
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
    visitedMuseums: List<Museum>,
    onMuseumClick: (Int) -> Unit = {}
) {
    Box {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            MuseumCardList(
                onMuseumClick = onMuseumClick,
                museums = visitedMuseums
            )
        }
    }
}
