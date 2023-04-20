package com.itmo.museum.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.itmo.museum.models.Museum
import com.itmo.museum.util.routePage

sealed class MuseumAppScreen(
    val route: String,
    val title: String,
) {
    sealed class BottomBarScreen(
        route: String,
        title: String,
        val icon: ImageVector
    ) : MuseumAppScreen(route, title) {
        object Museums : BottomBarScreen(
            route = "museums",
            title = "Museums",
            icon = Icons.Default.Home
        )

        object Visited : BottomBarScreen(
            route = "visited",
            title = "Visited",
            icon = Icons.Default.Check,
        )

        object About : BottomBarScreen(
            route = "about",
            title = "About",
            icon = Icons.Default.Info
        )
    }

    class MuseumProfile(museum: Museum) : MuseumAppScreen(
        route = museum.name,
        title = "${museum.name} profile"
    )

    class Route(museum: Museum) : MuseumAppScreen(
        route = museum.routePage,
        title = "Route to ${museum.name}"
    )

    object Greeting : MuseumAppScreen(
        route = "greeting",
        title = "Greeting"
    )
}
