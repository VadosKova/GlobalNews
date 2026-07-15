package com.example.globalnews.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.globalnews.domain.model.Article as DomainArticle

@Entity(tableName = "articles_table")
data class ArticleEntity(
    @PrimaryKey val url: String,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val category: String,
    val author: String?,
    val publishedAt: String?,
    val content: String?,
    val sourceName: String?
) {
    fun toDomain(): DomainArticle = DomainArticle(
        url = url,
        title = title ?: "No title",
        description = description ?: "No description",
        urlToImage = urlToImage,
        category = category,
        author = author ?: "Unknown author",
        publishedAt = publishedAt,
        content = content,
        sourceName = sourceName ?: "Unknown source"
    )
}