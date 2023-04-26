package com.itmo.museum.data

import com.itmo.museum.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserStream(id: Int): Flow<User?>

    fun getAllUsersStream(): Flow<List<User>>

    suspend fun insertUser(user: User)
}
