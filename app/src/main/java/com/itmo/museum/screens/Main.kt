package com.itmo.museum.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.itmo.museum.R
import com.itmo.museum.models.MapViewModel
import com.itmo.museum.navigation.MuseumAppScreen
import com.itmo.museum.navigation.NavGraph
import com.itmo.museum.util.SemanticKeys

@ExperimentalComposeUiApi
@Composable
fun MainScreen(mapViewModel: MapViewModel) {
    val navController = rememberNavController()
    NavGraph(
        navController = navController,
        mapViewModel = mapViewModel
    )
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        MuseumAppScreen.BottomBarScreen.Museums,
        MuseumAppScreen.BottomBarScreen.Visited,
        MuseumAppScreen.BottomBarScreen.About,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: MuseumAppScreen.BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        modifier = Modifier
            .testTag(stringResource(id = R.string.bottom_bar_item))
            .semantics { set(SemanticKeys.BottomBarItem, screen.title) },
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
    )
}
