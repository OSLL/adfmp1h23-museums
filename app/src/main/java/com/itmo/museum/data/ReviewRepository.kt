package com.itmo.museum.data

import com.itmo.museum.models.UserReview
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun getReviewStream(id: Int): Flow<UserReview?>

    fun getAllReviewsStream(): Flow<List<UserReview>>

    fun getReviewsStream(museumId: Int): Flow<List<UserReview>>

    fun getReviewBy(userId: Int, museumId: Int): Flow<UserReview?>

    suspend fun insertReview(review: UserReview)

    suspend fun deleteAll()
}
