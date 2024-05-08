package com.vk.usersapp.feature.feed.api

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class UsersRepositoryTest {
    private lateinit var usersRepository: UsersRepository

    @Before
    fun setup() {
        usersRepository = UsersRepository()
    }

//    Calling getUsers() should return non-empty list with 30 or less users
    @Test
    fun testGetUsersNotEmpty() {
        val users = runBlocking { usersRepository.getUsers() }

        assertTrue(users.isNotEmpty() && users.size <= 30)
    }

//    Calling searchUsers() with empty string should return all users
//    and should be equals to result of getUsers()
    @Test
    fun testSearchUsersEmptyQuery() {
        val usersSearch = runBlocking { usersRepository.searchUsers("") }
        val usersAll = runBlocking { usersRepository.getUsers() }

        assertTrue(usersSearch == usersAll)

    }

//    Calling searchUsers() with first name of existing user as argument
//    should return non-empty list of users
    @Test
    fun testSearchFirstNameNonEmptyList() {
        val query = runBlocking { usersRepository.getUsers() }.random().firstName
        val searchResult = runBlocking { usersRepository.searchUsers(query) }

        assertTrue(searchResult.isNotEmpty())
    }

//    Calling searchUsers() with first name of existing user as argument
//    should return non-empty list of users
    @Test
    fun testSearchLastNameNonEmptyList() {
        val query = runBlocking { usersRepository.getUsers() }.random().lastName
        val searchResult = runBlocking { usersRepository.searchUsers(query) }

        assertTrue(searchResult.isNotEmpty())
    }

//    Calling searchResult() with string that is not equal to first or last name
//    of any existing user should return empty list
    @Test
    fun testSearchRandomEmptyList() {
        val users = runBlocking { usersRepository.getUsers() }
        var query = Random.nextLong().toString()
        while(users.any { it.firstName == query || it.lastName == query }) {
            query = Random.nextLong().toString()
        }
        val searchResult = runBlocking { usersRepository.searchUsers(query) }

        assertTrue(searchResult.isEmpty())
    }
}