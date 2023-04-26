package com.itmo.museum.data

import com.itmo.museum.models.Museum
import kotlinx.coroutines.flow.Flow

class OfflineMuseumRepository(private val museumDao: MuseumDao) : MuseumRepository {
    override fun getMuseumStream(id: Int): Flow<Museum?> {
        return museumDao.getMuseum(id)
    }

    override fun getAllMuseumsStream(): Flow<List<Museum>> {
        return museumDao.getAllMuseums()
    }

    override fun getVisitedMuseumsStream(): Flow<List<Museum>> {
        return museumDao.getVisitedMuseums()
    }

    override suspend fun insertMuseum(museum: Museum) {
        museumDao.insert(museum)
    }

    override suspend fun updateMuseum(museum: Museum) {
        museumDao.update(museum)
    }

    override suspend fun markAllAsNotVisited() {
        museumDao.markAllAsNotVisited()
    }
}
