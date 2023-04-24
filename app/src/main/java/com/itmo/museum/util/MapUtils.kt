package com.itmo.museum.util

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.widget.Toast
import com.itmo.museum.R
import com.itmo.museum.models.MuseumDetails

fun makeLocationPermissionDeniedToast(context: Context) {
    val message = context.getString(R.string.location_permission_denied)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun makeLocationNotFoundToast(context: Context) {
    val message = context.getString(R.string.user_location_not_found)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun drawRoute(origin: Location, destination: MuseumDetails, context: Context) {
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
