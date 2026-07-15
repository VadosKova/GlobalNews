package com.example.globalnews.domain.usecase

import com.example.globalnews.domain.model.Article
import com.example.globalnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSearchArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(): Flow<List<Article>> = repository.getSearchArticles()
}

class GetSortedCategoryArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(category: String, sortOrder: String): Flow<List<Article>> {
        return repository.getArticlesByCategory(category).map { articles ->
            articles.sortAndPrioritize(sortOrder)
        }
    }

    private fun List<Article>.sortAndPrioritize(sortOrder: String): List<Article> {
        val (richContent, poorContent) = this.partition { article ->
            !article.urlToImage.isNullOrBlank() &&
                    !article.description.isBlank() &&
                    article.description != "No description"
        }

        val sortByPopularity = sortOrder == "popularity"

        val sortedRich = if (sortByPopularity) {
            richContent.sortedByDescending { it.description.length }
        } else {
            richContent.sortedByDescending { it.publishedAt }
        }

        val sortedPoor = if (sortByPopularity) {
            poorContent.sortedByDescending { it.description.length }
        } else {
            poorContent.sortedByDescending { it.publishedAt }
        }

        return sortedRich + sortedPoor
    }
}

class RefreshSearchNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(query: String, page: Int) {
        if (query.isBlank()) {
            repository.refreshTopHeadlines(page)
        } else {
            repository.refreshSearchNews(query, page)
        }
    }
}

class RefreshCategoryNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(category: String, page: Int) {
        repository.refreshCategoryNews(category, page)
    }
}

class NewsUseCases @Inject constructor(
    val getSearchArticles: GetSearchArticlesUseCase,
    val getSortedCategoryArticles: GetSortedCategoryArticlesUseCase,
    val refreshSearchNews: RefreshSearchNewsUseCase,
    val refreshCategoryNews: RefreshCategoryNewsUseCase
)