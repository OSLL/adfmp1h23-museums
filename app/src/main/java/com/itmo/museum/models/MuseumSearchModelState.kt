package com.itmo.museum.models


data class MuseumSearchModelState(
    val searchText: String = "",
    val museums: List<Museum> = arrayListOf(),
    val showProgressBar: Boolean = false
) {

    companion object {
        val Empty = MuseumSearchModelState()
    }

}