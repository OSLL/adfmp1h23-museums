package com.itmo.museum.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itmo.museum.data.MuseumDataProvider
import com.itmo.museum.elements.MuseumAppTopBar
import com.itmo.museum.elements.MuseumIndexCard
import com.itmo.museum.models.Museum

@Composable
@Preview
fun MuseumsScreen(
    onBackClicked: () -> Unit = {},
    onAboutClicked: () -> Unit = {},
    onMuseumClicked: (museum: String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            MuseumAppTopBar(
                titleText = "Museums",
                onBackClicked = onBackClicked,
                onAboutClicked = onAboutClicked
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(state = ScrollState(0))
                .background(Color.Magenta),
            contentAlignment = Alignment.Center
        ) {
            MuseumCardList(
                modifier = Modifier,
                onMuseumClicked = onMuseumClicked,
                museums = MuseumDataProvider.defaultProvider.museums
            )
        }
    }
}

@Preview
@Composable
fun MuseumCardList(
    modifier: Modifier = Modifier,
    onMuseumClicked: (museum: String) -> Unit = {},
    museums: List<Museum> = MuseumDataProvider.defaultProvider.museums,
) {
    val museumPairs = museums.chunked(2)
    Column(modifier = modifier) {
        museumPairs.forEach { chunk ->
            if (chunk.size == 2) {
                val (left, right) = chunk
                Row(modifier = Modifier) {
                    RowMuseumIndexCard(onMuseumClicked = onMuseumClicked, museum = left)
                    RowMuseumIndexCard(onMuseumClicked = onMuseumClicked, museum = right)
                }
            } else {
                Row(modifier = Modifier) {
                    RowMuseumIndexCard(
                        onMuseumClicked = onMuseumClicked,
                        weight = 1.0f,
                        museum = chunk.first()
                    )
                    Box(modifier = Modifier.weight(1.0f)) {}
                }
            }
        }
    }
}

@Composable
private fun RowScope.RowMuseumIndexCard(
    onMuseumClicked: (museum: String) -> Unit = {},
    weight: Float = 1.0f,
    museum: Museum
) {
    MuseumIndexCard(
        modifier = Modifier
            .weight(weight)
            .padding(2.dp)
            .clickable { onMuseumClicked(museum.name) },
        museum = museum
    )
}