package com.itmo.museum.elements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import com.itmo.museum.models.MuseumSearchModelState
import com.itmo.museum.models.MuseumSearchViewModel
import com.itmo.museum.screens.MuseumCardList
import com.itmo.museum.util.rememberFlowWithLifecycle

@ExperimentalComposeUiApi
@Composable
fun MuseumSearchUI(
    museumSearchViewModel: MuseumSearchViewModel,
    onSearchTextChanged: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onMuseumClick: (String) -> Unit = {},
) {
    val museumSearchModelState by rememberFlowWithLifecycle(museumSearchViewModel.museumSearchModelState)
        .collectAsState(initial = MuseumSearchModelState.Empty)

    SearchBarUI(
        searchText = museumSearchModelState.searchText,
        placeholderText = "Search museums",
        onSearchTextChanged = onSearchTextChanged,
        onBackClick = onBackClick,
        onClearClick = onClearClick,
        matchesFound = museumSearchModelState.museums.isNotEmpty()
    ) {
        MuseumCardList(
            museums = museumSearchModelState.museums,
            onMuseumClick = onMuseumClick
        )
    }
}