package com.itmo.museum

import android.app.Application
import com.itmo.museum.data.AppContainer
import com.itmo.museum.data.AppDataContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MuseumsApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
