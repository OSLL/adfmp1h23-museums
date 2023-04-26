package com.itmo.museum.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itmo.museum.data.MuseumRepository
import kotlinx.coroutines.flow.*

class MuseumSearchViewModel constructor(
    museumRepository: MuseumRepository
) : ViewModel() {
    private val allMuseums: StateFlow<List<Museum>> =
        museumRepository.getAllMuseumsStream()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = listOf()
            )

    private val searchText: MutableStateFlow<String> = MutableStateFlow("")
    private var showProgressBar: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val museumSearchModelState = combine(
        searchText,
        allMuseums,
        showProgressBar
    ) { text, allMuseums, showProgress ->
        val newMatchedMuseums = when {
            text.isEmpty() -> allMuseums
            else -> allMuseums.filter { museum ->
                museum.name.contains(text, true)
            }
        }

        MuseumSearchModelState(
            searchText = text,
            museums = newMatchedMuseums,
            showProgressBar = showProgress
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = MuseumSearchModelState()
    )

    fun onSearchTextChanged(changedSearchText: String) {
        searchText.value = changedSearchText
    }

    fun onClearClick() {
        searchText.value = ""
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
