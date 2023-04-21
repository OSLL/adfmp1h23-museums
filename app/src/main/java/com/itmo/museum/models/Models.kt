package com.itmo.museum.models

import com.itmo.museum.R

data class Rating(
    val count: Int = 0,
    val average: Float = 0f
)

data class Museum(
    val name: String,
    val address: String,
    val info: String,
    val imageId: Int,
    val reviews: List<UserReview> = emptyList()
)

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
    val rating: Float,
    val text: String,
)
