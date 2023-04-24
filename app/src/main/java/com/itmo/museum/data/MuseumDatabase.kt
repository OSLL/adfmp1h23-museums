package com.itmo.museum.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.itmo.museum.models.Museum
import com.itmo.museum.models.User
import com.itmo.museum.models.UserReview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Museum::class, UserReview::class, User::class], version = 1, exportSchema = false)
abstract class MuseumDatabase : RoomDatabase() {
    abstract fun museumDao(): MuseumDao
    abstract fun reviewDao(): ReviewDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: MuseumDatabase? = null

        fun getDatabase(context: Context): MuseumDatabase {
            return Instance ?: synchronized(this) {
                Instance ?: Room
                    .databaseBuilder(context, MuseumDatabase::class.java, "museum_database")
                    .addCallback(prepopulateDatabaseCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }

        private fun prepopulateDatabaseCallback(context: Context) = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val museumDao = getDatabase(context).museumDao()
                    MuseumDataProvider.defaultProvider.museums.forEach {
                        museumDao.insert(it)
                    }
                }
            }
        }
    }
}
