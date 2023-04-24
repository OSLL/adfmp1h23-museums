package com.itmo.museum.models

import androidx.lifecycle.ViewModel
import com.itmo.museum.data.MuseumDataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class MuseumSearchViewModel @Inject constructor() : ViewModel() {
    private var allMuseums: MutableList<Museum> = ArrayList<Museum>()
    private val searchText: MutableStateFlow<String> = MutableStateFlow("")
    private var showProgressBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var matchedMuseums: MutableStateFlow<List<Museum>> =
        MutableStateFlow(arrayListOf())

    val museumSearchModelState = combine(
        searchText,
        matchedMuseums,
        showProgressBar
    ) { text, matchedMuseums, showProgress ->

        MuseumSearchModelState(
            text,
            matchedMuseums,
            showProgress
        )
    }


    init {
        retrieveMuseums()
    }

    private fun retrieveMuseums() {
        allMuseums += MuseumDataProvider.defaultProvider.museums
        matchedMuseums.value = allMuseums
    }

    fun onSearchTextChanged(changedSearchText: String) {
        searchText.value = changedSearchText
        if (changedSearchText.isEmpty()) {
            matchedMuseums.value = allMuseums
            return
        }
        val museumsFromSearch = allMuseums.filter { museum ->
            museum.name.contains(changedSearchText, true) ||
                    museum.address.contains(changedSearchText, true)
        }
        matchedMuseums.value = museumsFromSearch
    }

    fun onClearClick() {
        searchText.value = ""
        matchedMuseums.value = arrayListOf()
    }
}
