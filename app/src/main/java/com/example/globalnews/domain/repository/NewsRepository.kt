package com.example.globalnews.domain.repository

import com.example.globalnews.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getSearchArticles(): Flow<List<Article>>
    fun getArticlesByCategory(category: String): Flow<List<Article>>
    suspend fun refreshTopHeadlines(page: Int)
    suspend fun refreshSearchNews(query: String, page: Int)
    suspend fun refreshCategoryNews(category: String, page: Int)
}