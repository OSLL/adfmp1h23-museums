package com.itmo.museum.models

import com.itmo.museum.R

data class Rating(
    val count: Int,
    val average: Double
)

data class Museum(
    val name: String,
    val address: String,
    val info: String,
    val imageId: Int,
    val reviews: List<UserReview> = emptyList()
) {
    val rating: Rating by lazy {
        val average = reviews.map { it.rating }.average()
        Rating(reviews.size, average)
    }
}

data class User(
    val name: String,
    val profilePictureId: Int,
) {
    companion object {
        val Anonymous = User(
            name = "Anonymous",
            profilePictureId = R.drawable.default_user,
        )
    }
}

data class UserReview(
    val user: User,
    val rating: Short,
    val text: String,
)
