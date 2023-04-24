package com.itmo.museum.data

import com.itmo.museum.models.Museum
import kotlinx.coroutines.flow.Flow

interface MuseumRepository {
    fun getMuseumStream(id: Int): Flow<Museum?>

    fun getAllMuseumsStream(): Flow<List<Museum>>

    fun getVisitedMuseumsStream(): Flow<List<Museum>>

    suspend fun insertMuseum(museum: Museum)

    suspend fun updateMuseum(museum: Museum)
}
