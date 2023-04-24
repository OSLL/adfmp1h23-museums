package com.itmo.museum.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itmo.museum.data.MuseumDataProvider
import com.itmo.museum.elements.MuseumIndexCard
import com.itmo.museum.models.Museum

@Preview
@Composable
fun MuseumCardList(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.LightGray,
    onMuseumClick: (museum: String) -> Unit = {},
    museums: List<Museum> = MuseumDataProvider.defaultProvider.museums,
) {
    val museumPairs = museums.chunked(2)
    Column(
        modifier = modifier
            .background(color = backgroundColor)
            .verticalScroll(state = ScrollState(0))
    ) {
        museumPairs.forEach { chunk ->
            if (chunk.size == 2) {
                val (left, right) = chunk
                Row(modifier = Modifier) {
                    RowMuseumIndexCard(onMuseumClick = onMuseumClick, museum = left)
                    RowMuseumIndexCard(onMuseumClick = onMuseumClick, museum = right)
                }
            } else {
                Row(modifier = Modifier) {
                    RowMuseumIndexCard(
                        onMuseumClick = onMuseumClick,
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
    onMuseumClick: (museum: String) -> Unit = {},
    weight: Float = 1.0f,
    museum: Museum
) {
    MuseumIndexCard(
        modifier = Modifier
            .weight(weight)
            .padding(2.dp),
        museum = museum,
        onClick = onMuseumClick
    )
}
