package com.itmo.museum.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

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

    sealed class WithArgs(
        route: String,
        title: String,
    ) : MuseumAppScreen(route, title) {
        abstract val routeWithArgs: String

        object MuseumProfile : WithArgs(
            route = "museums",
            title = "Museum profile"
        ) {
            const val museumIdArg: String = "museumId"
            override val routeWithArgs: String = "$route/{$museumIdArg}"
        }

        object AddReview : WithArgs(
            route = "add-review",
            title = "Add review",
        ) {
            const val museumIdArg: String = "museumId"
            override val routeWithArgs = "$route/{$museumIdArg}"
        }
    }

    object Greeting : MuseumAppScreen(
        route = "greeting",
        title = "Greeting"
    )
}
