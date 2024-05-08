package com.vk.usersapp.feature.feed.api

import com.vk.usersapp.feature.feed.model.User
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val api: UsersApi
) {

    suspend fun getUsers(): List<User> {
        return api.getUsers(
            limit = 30,
            skip = 0
        ).users
    }

    suspend fun searchUsers(query: String): List<User> {
        return api.searchUsers(
            query = query,
            limit = 30,
            skip = 0
        ).users
    }
}