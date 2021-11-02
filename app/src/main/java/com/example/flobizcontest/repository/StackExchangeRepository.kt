package com.example.flobizcontest.repository

import com.example.flobizcontest.service.StackApi
import javax.inject.Inject

class StackExchangeRepository @Inject constructor(private val stackApi: StackApi) {

    suspend fun fetchQuestions() = stackApi.fetchQuestions()

    suspend fun searchQuestions(query: String) = stackApi.searchQuestions(query = query)

    suspend fun searchWithFilterTags(tags: String) = stackApi.searchWithFilterTags(tags = tags)

}