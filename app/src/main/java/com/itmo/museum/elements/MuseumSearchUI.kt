package com.itmo.museum.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.itmo.museum.models.*
import com.itmo.museum.screens.BottomBar
import com.itmo.museum.screens.MuseumCardList

@ExperimentalComposeUiApi
@Composable
fun MuseumSearchUI(
    navController: NavHostController,
    onBackClick: () -> Unit = {},
    onMuseumClick: (Int) -> Unit = {},
    viewModel: MuseumSearchViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val museumSearchModelState by viewModel.museumSearchModelState.collectAsState()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        SearchBarUI(
            modifier = Modifier.padding(innerPadding),
            searchText = museumSearchModelState.searchText,
            placeholderText = "Search museums",
            onSearchTextChanged = { viewModel.onSearchTextChanged(it) },
            onBackClick = onBackClick,
            onClearClick = { viewModel.onClearClick() },
            matchesFound = museumSearchModelState.museums.isNotEmpty()
        ) {
            MuseumCardList(
                museums = museumSearchModelState.museums,
                onMuseumClick = onMuseumClick
            )
        }
    }
}