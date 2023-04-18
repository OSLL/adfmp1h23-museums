package com.itmo.museum.util

import com.itmo.museum.models.Museum
import com.itmo.museum.elements.RouteScreen

/**
 * Returns the route to [RouteScreen] for the given museum.
 */
val Museum.routePage: String
    get() = "$name-route"
