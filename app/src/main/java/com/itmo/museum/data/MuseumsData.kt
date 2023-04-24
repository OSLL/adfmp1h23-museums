package com.itmo.museum.data

import com.itmo.museum.R
import com.itmo.museum.elements.placeholderText
import com.itmo.museum.models.Museum

interface MuseumDataProvider {
    val museums: List<Museum>

    companion object {
        val defaultProvider: MuseumDataProvider = MuseumDataRegularProvider()
    }
}

/**
 * This provider returns a list of museums defined in the source code.
 */
private class MuseumDataRegularProvider : MuseumDataProvider {
    override val museums: List<Museum>
        // TODO: use multiple different museums
        get() = List(10) { HERMITAGE }

    private companion object {
        // TODO: define multiple actual museums
        val HERMITAGE = Museum(
            name = "Hermitage",
            address = "Дворцовая площадь",
            isVisited = false,
            info = placeholderText,
            imageId = R.drawable.hermitage,
            latitude = 59.939896,
            longitude = 30.314559
        )
    }
}
