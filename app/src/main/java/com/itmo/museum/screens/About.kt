package com.itmo.museum.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.itmo.museum.R
import com.itmo.museum.elements.MuseumAppTopBar
import com.itmo.museum.ui.theme.MuseumTheme

@Composable
fun AboutScreen(
    navController: NavHostController,
    onBackClicked: () -> Unit = {},
) {
    MuseumTheme {
        Scaffold(
            topBar = {
                MuseumAppTopBar(
                    titleText = "Museums App",
                    onBackClicked = onBackClicked,
                )
            },
            bottomBar = { BottomBar(navController = navController) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxHeight(),
            ) {
                Text(
                    modifier = Modifier
                        .padding(10.dp)
                        .testTag(stringResource(id = R.string.about_page_info)),
                    text = stringResource(id = R.string.about_page_content),
                    fontSize = MaterialTheme.typography.body1.fontSize,
                )
            }
        }
    }
}
