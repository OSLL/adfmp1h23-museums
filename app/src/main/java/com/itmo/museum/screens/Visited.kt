package com.itmo.museum.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.itmo.museum.elements.MuseumIndexCard
import com.itmo.museum.models.AppViewModel

@Composable
fun VisitedScreen(viewModel: AppViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        val visitedMuseums = uiState.visitedMuseums
        if (visitedMuseums.isEmpty()) {
            Text(
                text = "No museums visited yet",
                fontSize = MaterialTheme.typography.h4.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        } else {
            visitedMuseums.forEach { museum ->
                MuseumIndexCard(museum = museum)
            }
        }
    }
}