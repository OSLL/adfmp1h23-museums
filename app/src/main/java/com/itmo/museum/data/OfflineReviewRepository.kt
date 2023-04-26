package com.itmo.museum.data

import com.itmo.museum.models.UserReview
import kotlinx.coroutines.flow.Flow

class OfflineReviewRepository(private val reviewDao: ReviewDao) : ReviewRepository {
    override fun getReviewStream(id: Int): Flow<UserReview?> {
        return reviewDao.getReview(id)
    }

    override fun getAllReviewsStream(): Flow<List<UserReview>> {
        return reviewDao.getAllReviews()
    }

    override fun getReviewsStream(museumId: Int): Flow<List<UserReview>> {
        return reviewDao.getReviewsFor(museumId)
    }

    override fun getReviewBy(userId: Int, museumId: Int): Flow<UserReview?> {
        return reviewDao.getReviewBy(userId, museumId)
    }

    override suspend fun insertReview(review: UserReview) {
        reviewDao.insert(review)
    }

    override suspend fun deleteAll() {
        reviewDao.deleteAll()
    }
}
