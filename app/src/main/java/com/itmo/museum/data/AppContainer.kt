package com.itmo.museum.data

import android.content.Context

interface AppContainer {
    val museumRepository: MuseumRepository
    val reviewRepository: ReviewRepository
    val userRepository: UserRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val museumRepository: MuseumRepository by lazy {
        OfflineMuseumRepository(MuseumDatabase.getDatabase(context).museumDao())
    }

    override val reviewRepository: ReviewRepository by lazy {
        OfflineReviewRepository(MuseumDatabase.getDatabase(context).reviewDao())
    }

    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(MuseumDatabase.getDatabase(context).userDao())
    }
}
