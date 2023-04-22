package com.itmo.museum.util

import com.itmo.museum.models.Museum
import com.itmo.museum.models.AppUiState
import com.itmo.museum.models.Rating
import com.itmo.museum.models.UserReview

fun AppUiState.getRatingOf(museum: Museum): Rating = rating[museum] ?: Rating()

fun AppUiState.getReviewsFor(museum: Museum): List<UserReview> = reviews[museum] ?: emptyList()
