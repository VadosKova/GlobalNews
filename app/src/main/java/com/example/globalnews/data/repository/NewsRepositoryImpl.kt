package com.example.globalnews.data.repository

import com.example.globalnews.data.local.ArticleEntity
import com.example.globalnews.data.local.NewsDao
import com.example.globalnews.data.remote.NewsApiService
import com.example.globalnews.domain.model.Article as DomainArticle
import com.example.globalnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao
) : NewsRepository {

    private val apiKey = "1c5a72c31ce040b4b4ce7a04e490ade7"

    override fun getSearchArticles(): Flow<List<DomainArticle>> {
        return newsDao.getSearchArticles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getArticlesByCategory(category: String): Flow<List<DomainArticle>> {
        return newsDao.getArticlesByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshTopHeadlines(page: Int) {
        try {
            val response = apiService.getTopHeadlinesByCategory("general", page, 20, apiKey)
            val entities = response.articlesList.mapNotNull { it.toEntity("search") }
            newsDao.clearArticlesByCategory("search")
            newsDao.insertArticles(entities)
        } catch (e: Exception) { e.printStackTrace() }
    }

    override suspend fun refreshSearchNews(query: String, page: Int) {
        try {
            val response = apiService.searchNews(query, page, 20, apiKey)
            val entities = response.articlesList.mapNotNull { it.toEntity("search") }
            newsDao.clearArticlesByCategory("search")
            newsDao.insertArticles(entities)
        } catch (e: Exception) { e.printStackTrace() }
    }

    override suspend fun refreshCategoryNews(category: String, page: Int) {
        try {
            val response = apiService.getTopHeadlinesByCategory(category, page, 20, apiKey)
            val entities = response.articlesList.mapNotNull { it.toEntity(category) }
            newsDao.clearArticlesByCategory(category)
            newsDao.insertArticles(entities)
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun com.example.globalnews.data.model.NetworkArticle.toEntity(category: String): ArticleEntity? {
        val validUrl = this.url ?: return null
        return ArticleEntity(
            url = validUrl,
            title = this.title,
            description = this.description,
            urlToImage = this.urlToImage,
            category = category,
            author = this.author,
            publishedAt = this.publishedAt,
            content = this.content,
            sourceName = this.source?.name
        )
    }
}