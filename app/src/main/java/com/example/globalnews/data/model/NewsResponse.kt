package com.example.globalnews.data.model

import com.google.gson.annotations.SerializedName


data class NewsResponse(
    @SerializedName("articles")
    val articlesList: List<NetworkArticle>
)

data class NetworkArticle(
    val url: String?,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val author: String?,
    val publishedAt: String?,
    val content: String?,
    val source: NetworkSource?
)

data class NetworkSource(
    val name: String
)