package com.itmo.museum.util

import android.content.Context
import android.widget.Toast
import com.itmo.museum.R
import com.itmo.museum.models.*

fun Context.makeReviewAlreadyPresentToast() {
    val message = getString(R.string.review_already_present)
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

val List<UserReviewDetails>.rating: Rating
    get() = Rating(
        count = size,
        average = map { it.rating }.average().toFloat()
    )

fun MuseumDetails.toMuseum(): Museum {
    return Museum(
        id = id,
        name = name,
        address = address,
        isVisited = isVisited,
        info = info,
        imageId = imageId,
        latitude = location.latitude,
        longitude = location.longitude
    )
}

fun Museum.toMuseumDetails(): MuseumDetails {
    return MuseumDetails(
        id = id,
        name = name,
        address = address,
        isVisited = isVisited,
        info = info,
        imageId = imageId,
        latitude = location.latitude,
        longitude = location.longitude
    )
}

fun UserReviewDetails.toUserReview(): UserReview {
    return UserReview(
        id = id,
        userId = userId,
        userName = userName,
        museumId = museumId,
        rating = rating,
        text = text
    )
}

fun UserReview.toUserReviewDetails(): UserReviewDetails {
    return UserReviewDetails(
        id = id,
        userId = userId,
        userName = userName,
        museumId = museumId,
        rating = rating,
        text = text
    )
}
