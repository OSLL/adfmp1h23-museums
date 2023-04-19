package com.itmo.museum.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itmo.museum.elements.MuseumAppTopBar
import com.itmo.museum.ui.theme.MuseumTheme

@Preview
@Composable
fun AboutScreen(
    onBackClicked: () -> Unit = {},
) {
    MuseumTheme {
        Scaffold(
            topBar = {
                MuseumAppTopBar(
                    titleText = "Museums App",
                    onBackClicked = onBackClicked,
                    onAboutClicked = {}
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxHeight(),
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = """
                        This app allows a user to search through the many museums of Saint Petersburg.
                        Each museum has its own profile where one can find some useful info about a museum
                        along with reviews from other users.
        
                        If you find a certain museum interesting, you can request a route to it on the map
                        so that you can visit it yourself.
        
                        A user can also mark the museums they have already visited. Such museums can be found
                        on the 'Visited' page of the app.
                        
                        Authors: Arsen Nagdalian and Anton Sudov
                    """.trimIndent(),
                    fontSize = MaterialTheme.typography.body1.fontSize,
                )
            }
        }
    }
}
