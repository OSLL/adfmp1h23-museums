package com.itmo.museum.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Museums : BottomBarScreen(
        route = "museums",
        title = "Museums",
        icon = Icons.Default.Home
    )
    object Visited : BottomBarScreen(
        route = "visited",
        title = "Visited",
        icon = Icons.Default.ArrowBack,
    )
}
