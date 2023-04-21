package com.itmo.museum.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.itmo.museum.models.AppViewModel
import com.itmo.museum.models.MuseumSearchModelState
import com.itmo.museum.models.MuseumSearchViewModel
import com.itmo.museum.screens.BottomBar
import com.itmo.museum.screens.MuseumCardList
import com.itmo.museum.util.rememberFlowWithLifecycle

@ExperimentalComposeUiApi
@Composable
fun MuseumSearchUI(
    navController: NavHostController,
    viewModel: AppViewModel,
    museumSearchViewModel: MuseumSearchViewModel,
    onSearchTextChanged: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onMuseumClick: (String) -> Unit = {},
) {
    val museumSearchModelState by rememberFlowWithLifecycle(museumSearchViewModel.museumSearchModelState)
        .collectAsState(initial = MuseumSearchModelState.Empty)
Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        SearchBarUI(
            modifier = Modifier.padding(innerPadding),
            searchText = museumSearchModelState.searchText,
            placeholderText = "Search museums",
            onSearchTextChanged = onSearchTextChanged,
            onBackClick = onBackClick,
            onClearClick = onClearClick,
            matchesFound = museumSearchModelState.museums.isNotEmpty()
        ) {
            MuseumCardList(
                viewModel = viewModel,
                museums = museumSearchModelState.museums,
                onMuseumClick = onMuseumClick
            )
        }
    }
}