package com.itmo.museum.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.itmo.museum.data.MuseumDataProvider
import com.itmo.museum.elements.MuseumIndexCard

@Composable
fun MuseumsScreen(
    onMuseumClicked: (museum: String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(state = ScrollState(0))
            .background(Color.Magenta),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier) {
            MuseumDataProvider.defaultProvider.museums.forEach { museum ->
                MuseumIndexCard(
                    modifier = Modifier.clickable { onMuseumClicked(museum.name) },
                    museum = museum
                )
            }
        }
    }
}
