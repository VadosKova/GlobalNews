package com.example.globalnews.domain.model

data class Article(
    val url: String,
    val title: String,
    val description: String,
    val urlToImage: String?,
    val category: String,
    val author: String,
    val publishedAt: String?,
    val content: String?,
    val sourceName: String?
)