package com.itmo.museum.models

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState(visitedMuseums = persistentListOf()))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun addVisitedMuseum(museum: Museum) {
        _uiState.update { currentState ->
            currentState.copy(
                visitedMuseums = currentState.visitedMuseums.add(museum.name)
            )
        }
    }
}

data class AppUiState(
    val visitedMuseums: PersistentList<String>
)
