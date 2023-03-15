package com.itmo.museum.models

data class Rating(
    val count: Int,
    val average: Double
)

data class Museum(
    val name: String,
    val address: String,
    val info: String,
    val picture: Int,
    val rating: Rating
)