package com.example.globalnews.util

import com.example.globalnews.data.model.Article


fun List<Article>.sortAndPrioritize(sortOrder: String): List<Article> {
    val (richContent, poorContent) = this.partition { article ->
        !article.urlToImage.isNullOrBlank() &&
        !article.description.isNullOrBlank() &&
        article.description != "No description"
    }

    val sortByPopularity = sortOrder == "popularity"

    val sortedRich = if (sortByPopularity) {
        richContent.sortedByDescending { it.description?.length ?: 0 }
    } else {
        richContent.sortedByDescending { it.publishedAt }
    }

    val sortedPoor = if (sortByPopularity) {
        poorContent.sortedByDescending { it.description?.length ?: 0 }
    } else {
        poorContent.sortedByDescending { it.publishedAt }
    }

    return sortedRich + sortedPoor
}