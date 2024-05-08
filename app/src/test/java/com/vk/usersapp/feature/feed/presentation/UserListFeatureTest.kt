package com.vk.usersapp.feature.feed.presentation

import com.vk.usersapp.core.Retrofit
import com.vk.usersapp.feature.feed.api.UsersApi
import com.vk.usersapp.feature.feed.api.UsersRepository
import com.vk.usersapp.feature.feed.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserListFeatureTest {
    private val testUser = User(
        "John", "Doe",
        45, "image", "SPbPU"
    )
    private lateinit var userListFeature: UserListFeature
    private val api: UsersApi = Retrofit.getClient().create(UsersApi::class.java)
    private val reducer = UserListReducer()
    private val repository = UsersRepository(api)

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        userListFeature = UserListFeature(repository, reducer)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

//    Submitting UserListAction.Init results in UserListViewState.Loading
    @Test
    fun testSubmitInitAction() {
        userListFeature.submitAction(UserListAction.Init)
        assertTrue(userListFeature.viewStateFlow.value is UserListViewState.Loading)
    }

//    Submitting UserListAction.QueryChanged results in UserListViewState.Loading
    @Test
    fun testSubmitQueryChanged() {
        userListFeature.submitAction(UserListAction.QueryChanged("test"))
        assertTrue(userListFeature.viewStateFlow.value is UserListViewState.Loading)
    }

//    Submitting UserListAction.UsersLoaded with a non-empty list results
//    in UserListViewState.List with the same list
    @Test
    fun testSubmitUsersLoadedNonEmptyList() {
        val users = listOf(testUser, testUser)

        userListFeature.submitAction(UserListAction.UsersLoaded(users))
        assertTrue(
            userListFeature.viewStateFlow.value == UserListViewState.List(users)
        )
    }

//    Submitting UserListAction.UsersLoaded with an empty list results
//    in UserListViewState.List with an empty list
    @Test
    fun testSubmitUsersLoadedEmptyList() {
        val users = emptyList<User>()

        userListFeature.submitAction(UserListAction.UsersLoaded(users))
        assertTrue(
            (userListFeature.viewStateFlow.value as UserListViewState.List)
                .itemsList.isEmpty()
        )
    }

//    Submitting UserListAction.LoadError with a non-blank error message results in
//    UserListViewState.Error with the same error message
    @Test
    fun testSubmitLoadErrorNonBlankErrorMessage() {
        val errorMessage = "Error occurred"

        userListFeature.submitAction(UserListAction.LoadError(errorMessage))
        assertTrue(
            (userListFeature.viewStateFlow.value as UserListViewState.Error)
                .errorText == errorMessage
        )
    }

//    Submitting UserListAction.LoadError with a blank error message results in
//    UserListViewState.List with an empty list
    @Test
    fun testSubmitLoadErrorBlankErrorMessage() {
        val errorMessage = ""

        userListFeature.submitAction(UserListAction.LoadError(errorMessage))
        assertTrue(
            (userListFeature.viewStateFlow.value as UserListViewState.List)
                .itemsList.isEmpty()
        )
    }
}