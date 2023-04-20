package com.itmo.museum.elements

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import com.itmo.museum.models.Museum
import com.itmo.museum.screens.BottomBar
import com.itmo.museum.ui.theme.MuseumTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RouteScreen(
    navController: NavHostController,
    onBackClicked: () -> Unit = {},
    targetMuseum: Museum = defaultMuseum
) {
    val mapProperties = MapProperties(
        isMyLocationEnabled = true,
    )
    val cameraPositionState = rememberCameraPositionState()
    MuseumTheme {
        Scaffold(
            topBar = {
                MuseumAppTopBar(
                    titleText = "Route to ${targetMuseum.name}",
                    onBackClicked = onBackClicked,
                )
            },
            bottomBar = { BottomBar(navController = navController) }
        ) { innerPadding ->
            GoogleMap(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                properties = mapProperties,
                cameraPositionState = cameraPositionState
            )
        }
    }
}