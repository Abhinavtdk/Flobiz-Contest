package com.example.flobizcontest.service

import com.example.flobizcontest.utils.Constants
import com.example.flobizcontest.model.StackExchangeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StackApi {

    @GET("2.2/questions")
    suspend fun fetchQuestions(
        @Query("key")
        key: String = Constants.API_KEY,
        @Query("order")
        order: String = "desc",
        @Query("sort")
        sort: String = "activity",
        @Query("site")
        site: String = "stackoverflow",
        @Query("page")
        page: Int = 1,
        @Query("pageSize")
        pageSize: Int = 25
    ): Response<StackExchangeResponse>

    @GET("/2.3/search")
    suspend fun searchQuestions(
        @Query("key")
        key: String = Constants.API_KEY,
        @Query("intitle")
        query: String,
        @Query("order")
        order: String = "desc",
        @Query("sort")
        sort: String = "votes",
        @Query("site")
        site: String = "stackoverflow",
        @Query("page")
        page: Int = 1,
        @Query("pageSize")
        pageSize: Int = 25
    ): Response<StackExchangeResponse>

    @GET("/2.3/search")
    suspend fun searchWithFilterTags(
        @Query("key")
        key: String = Constants.API_KEY,
        @Query("tagged")
        tags: String,
        @Query("order")
        order: String = "desc",
        @Query("sort")
        sort: String = "votes",
        @Query("site")
        site: String = "stackoverflow",
        @Query("page")
        page: Int = 1,
        @Query("pageSize")
        pageSize: Int = 25
    ): Response<StackExchangeResponse>
}