package com.example.flobizcontest.di

import com.example.flobizcontest.service.ApiClient
import com.example.flobizcontest.service.StackApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun providesApiClient(): ApiClient = ApiClient()

    @Provides
    @Singleton
    fun providesStackApi(apiClient: ApiClient): StackApi =
        apiClient.retrofit.create(StackApi::class.java)



}