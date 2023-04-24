package com.itmo.museum.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.itmo.museum.elements.MuseumAppTopBar
import com.itmo.museum.models.AppViewModel
import com.itmo.museum.models.Museum

@Composable
fun VisitedScreen(
    onBackClicked: () -> Unit = {},
    onMuseumClick: (String) -> Unit = {},
    viewModel: AppViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            MuseumAppTopBar(
                titleText = "Visited",
                onBackClicked = onBackClicked,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Blue),
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
    onMuseumClick: (String) -> Unit = {}
) {
    Box {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            MuseumCardList(
                onMuseumClick = onMuseumClick,
                backgroundColor = Color.Blue,
                museums = visitedMuseums
            )
        }
    }
}
