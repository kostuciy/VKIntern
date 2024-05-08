package com.vk.usersapp.di

import com.vk.usersapp.core.Retrofit
import com.vk.usersapp.feature.feed.api.UsersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUsersApi(): UsersApi =
        Retrofit.getClient().create(UsersApi::class.java)

}