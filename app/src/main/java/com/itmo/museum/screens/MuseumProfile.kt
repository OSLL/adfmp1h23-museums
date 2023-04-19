package com.itmo.museum.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itmo.museum.elements.*
import com.itmo.museum.elements.defaultMuseum
import com.itmo.museum.models.Museum
import com.itmo.museum.ui.theme.MuseumTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun MuseumProfile(
    museum: Museum = defaultMuseum,
    onBackClicked: () -> Unit = {},
    onRouteClicked: () -> Unit = {}
) {
    MuseumTheme {
        Scaffold(
            topBar = {
                MuseumAppTopBar(
                    titleText = museum.name,
                    onBackClicked = onBackClicked
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .verticalScroll(state = ScrollState(0)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MuseumCard(museum = museum)
                MuseumInfo(museum = museum)
                ReviewList(museum.reviews)
                RouteButton(onClick = onRouteClicked)
            }
        }
    }
}
