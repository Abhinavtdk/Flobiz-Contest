package com.example.flobizcontest.model

data class StackExchangeResponse(
    val has_more: Boolean,
    val items: List<Item>,
    val quota_max: Int,
    val quota_remaining: Int
)