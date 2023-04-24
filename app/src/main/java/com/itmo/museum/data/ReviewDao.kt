package com.itmo.museum.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itmo.museum.models.UserReview
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE id = :id")
    fun getReview(id: Int): Flow<UserReview>

    @Query("SELECT * FROM reviews")
    fun getAllReviews(): Flow<List<UserReview>>

    @Query(
        "SELECT * FROM reviews WHERE museumId = :museumId"
    )
    fun getReviewsFor(museumId: Int): Flow<List<UserReview>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(review: UserReview)
}
