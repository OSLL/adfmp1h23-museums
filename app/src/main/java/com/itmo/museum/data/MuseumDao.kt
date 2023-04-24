package com.itmo.museum.data

import androidx.room.*
import com.itmo.museum.models.Museum
import kotlinx.coroutines.flow.Flow

@Dao
interface MuseumDao {
    @Query("SELECT * FROM museums WHERE id = :id")
    fun getMuseum(id: Int): Flow<Museum>

    @Query("SELECT * FROM museums ORDER BY name ASC")
    fun getAllMuseums(): Flow<List<Museum>>

    @Query("SELECT * FROM museums WHERE isVisited")
    fun getVisitedMuseums(): Flow<List<Museum>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(museum: Museum)

    @Update
    suspend fun update(museum: Museum)
}
