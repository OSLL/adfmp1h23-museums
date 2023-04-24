package com.itmo.museum.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.itmo.museum.models.MuseumDetails

fun drawRoute(origin: LatLng, destination: MuseumDetails, context: Context) {
    val request = buildString {
        append("https://www.google.com/maps/dir/")
        append("${origin.latitude} ${origin.longitude}/")
        append("${destination.name}, ${destination.address}")
    }

    val uri = Uri.parse(request)
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}
