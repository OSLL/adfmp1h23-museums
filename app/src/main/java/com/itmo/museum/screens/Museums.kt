package com.itmo.museum.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.itmo.museum.R
import com.itmo.museum.data.MuseumDataProvider
import com.itmo.museum.elements.MuseumIndexCard
import com.itmo.museum.models.Museum
import com.itmo.museum.util.SemanticKeys

@Composable
fun MuseumCardList(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.LightGray,
    onMuseumClick: (museumId: Int) -> Unit = {},
    museums: List<Museum> = MuseumDataProvider.defaultProvider(LocalContext.current).museums,
) {
    val museumPairs = museums.chunked(2)
    Column(
        modifier = modifier
            .testTag(stringResource(id = R.string.museum_list))
            .fillMaxSize()
            .background(color = backgroundColor)
            .verticalScroll(state = ScrollState(0))
    ) {
        museumPairs.forEach { chunk ->
            if (chunk.size == 2) {
                val (left, right) = chunk
                MuseumsFullRow(
                    onMuseumClick = onMuseumClick,
                    museums = arrayOf(left, right)
                )
            } else {
                MuseumsHalfRow(
                    onMuseumClick = onMuseumClick,
                    museum = chunk.first()
                )
            }
        }
    }
}

@Composable
private fun MuseumsFullRow(
    vararg museums: Museum,
    onMuseumClick: (museumId: Int) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .height(intrinsicSize = IntrinsicSize.Max)
    ) {
        museums.forEach { museum ->
            RowMuseumIndexCard(
                modifier = Modifier.fillMaxHeight(),
                onMuseumClick = onMuseumClick,
                museum = museum
            )
        }
    }
}

@Composable
private fun MuseumsHalfRow(
    museum: Museum,
    onMuseumClick: (museumId: Int) -> Unit
) {
    Row(
        modifier = Modifier
            .height(intrinsicSize = IntrinsicSize.Max)
    ) {
        RowMuseumIndexCard(
            modifier = Modifier.fillMaxHeight(),
            onMuseumClick = onMuseumClick,
            museum = museum
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1.0f)
        )
    }
}

@Composable
private fun RowScope.RowMuseumIndexCard(
    modifier: Modifier = Modifier,
    onMuseumClick: (museumId: Int) -> Unit = {},
    weight: Float = 1.0f,
    museum: Museum
) {
    MuseumIndexCard(
        modifier = modifier
            .weight(weight)
            .padding(2.dp)
            .semantics { set(SemanticKeys.MuseumCard, "museum-${museum.name}") },
        museum = museum,
        onClick = { onMuseumClick(museum.id) }
    )
}
