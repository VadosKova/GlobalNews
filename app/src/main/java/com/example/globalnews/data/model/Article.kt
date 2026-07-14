package com.example.globalnews.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles_table")
data class Article(
    @PrimaryKey val url: String,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val category: String,
    val author: String?,
    val publishedAt: String?,
    val content: String?,
    val sourceName: String?
)