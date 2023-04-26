package com.itmo.museum.data

import com.itmo.museum.models.User
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
    override fun getUserStream(id: Int): Flow<User?> {
        return userDao.getUser(id)
    }

    override fun getAllUsersStream(): Flow<List<User>> {
        return userDao.getAllUsers()
    }

    override suspend fun insertUser(user: User) {
        return userDao.insert(user)
    }

}
