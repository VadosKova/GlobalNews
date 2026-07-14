package com.example.globalnews.data.repository

import com.example.globalnews.data.local.NewsDao
import com.example.globalnews.data.model.Article
import com.example.globalnews.data.remote.NewsApiService
import kotlinx.coroutines.flow.Flow


class NewsRepository(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao
) {
    private val apiKey = "1c5a72c31ce040b4b4ce7a04e490ade7"
    val allArticles: Flow<List<Article>> = newsDao.getSearchArticles()

    fun getArticlesByCategory(category: String): Flow<List<Article>> {
        return newsDao.getArticlesByCategory(category)
    }

    suspend fun refreshTopHeadlines(page: Int) {
        try {
            val response = apiService.getTopHeadlinesByCategory(
                category = "general",
                page = page,
                pageSize = 20,
                apiKey = apiKey
            )
            val articlesRoom = response.articlesList.mapNotNull { networkArticle ->
                val url = networkArticle.url ?: return@mapNotNull null

                Article(
                    url = url,
                    title = networkArticle.title ?: "No title",
                    description = networkArticle.description ?: "No description",
                    urlToImage = networkArticle.urlToImage,
                    category = "search",
                    author = networkArticle.author ?: "Unknown author",
                    publishedAt = networkArticle.publishedAt,
                    content = networkArticle.content,
                    sourceName = networkArticle.source?.name ?: "Unknown source"
                )
            }
            newsDao.clearArticlesByCategory("search")
            newsDao.insertArticles(articlesRoom)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun refreshSearchNews(query: String, page: Int) {
        try {
            val response = apiService.searchNews(
                query = query,
                page = page,
                pageSize = 20,
                apiKey = apiKey
            )

            val articlesRoom = response.articlesList.mapNotNull { networkArticle ->
                val url = networkArticle.url ?: return@mapNotNull null

                Article(
                    url = url,
                    title = networkArticle.title ?: "No title",
                    description = networkArticle.description ?: "No description",
                    urlToImage = networkArticle.urlToImage,
                    category = "search",
                    author = networkArticle.author ?: "Unknown author",
                    publishedAt = networkArticle.publishedAt,
                    content = networkArticle.content,
                    sourceName = networkArticle.source?.name ?: "Unknown source"
                )
            }

            newsDao.clearArticlesByCategory("search")
            newsDao.insertArticles(articlesRoom)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun refreshCategoryNews(category: String, page: Int) {
        try {
            val response = apiService.getTopHeadlinesByCategory(
                category = category,
                page = page,
                pageSize = 20,
                apiKey = apiKey
            )

            val articlesRoom = response.articlesList.mapNotNull { networkArticle ->
                val url = networkArticle.url ?: return@mapNotNull null

                Article(
                    url = url,
                    title = networkArticle.title ?: "No title",
                    description = networkArticle.description ?: "No description",
                    urlToImage = networkArticle.urlToImage,
                    category = category,
                    author = networkArticle.author ?: "Unknown author",
                    publishedAt = networkArticle.publishedAt,
                    content = networkArticle.content,
                    sourceName = networkArticle.source?.name ?: "Unknown source"
                )
            }

            newsDao.clearArticlesByCategory(category)
            newsDao.insertArticles(articlesRoom)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}