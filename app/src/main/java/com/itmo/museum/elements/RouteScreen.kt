package com.itmo.museum.elements

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import com.itmo.museum.models.Museum
import com.itmo.museum.ui.theme.MuseumTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Preview
fun RouteScreen(
    targetMuseum: Museum = defaultMuseum
) {
    val mapProperties = MapProperties(
        isMyLocationEnabled = true,
    )
    val cameraPositionState = rememberCameraPositionState()
    MuseumTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Route") }
                )
            }
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                properties = mapProperties,
                cameraPositionState = cameraPositionState
            )
        }
    }
}