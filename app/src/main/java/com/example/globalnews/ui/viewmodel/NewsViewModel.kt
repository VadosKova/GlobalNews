package com.example.globalnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.globalnews.domain.model.Article
import com.example.globalnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.globalnews.util.sortAndPrioritize

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortBy = MutableStateFlow("publishedAt")
    val sortBy: StateFlow<String> = _sortBy.asStateFlow()

    private val _selectedCategory = MutableStateFlow("technology")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val searchArticles: Flow<List<Article>> = repository.allArticles
    val isLoading = MutableStateFlow(false)

    private val _searchCurrentPage = MutableStateFlow(1)
    val searchCurrentPage: StateFlow<Int> = _searchCurrentPage.asStateFlow()

    private val _categoryCurrentPage = MutableStateFlow(1)
    val categoryCurrentPage: StateFlow<Int> = _categoryCurrentPage.asStateFlow()

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun executeSearch() {
        loadSearchPage(1)
    }

    fun loadSearchPage(page: Int) {
        _searchCurrentPage.value = page
        viewModelScope.launch {
            isLoading.value = true
            val query = _searchQuery.value.trim()

            if (query.isEmpty()) {
                repository.refreshTopHeadlines(page = page)
            } else {
                repository.refreshSearchNews(query = query, page = page)
            }
            isLoading.value = false
        }
    }

    fun loadCategoryPage(page: Int) {
        _categoryCurrentPage.value = page
        viewModelScope.launch {
            isLoading.value = true
            repository.refreshCategoryNews(category = _selectedCategory.value, page = page)
            isLoading.value = false
        }
    }

    fun getArticlesByCategory(category: String): Flow<List<Article>> {
        return repository.getArticlesByCategory(category).combine(_sortBy) { articles, sortOrder ->
            articles.sortAndPrioritize(sortOrder)
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        loadCategoryPage(1)
    }

    fun changeSortOrder(order: String) {
        _sortBy.value = order
    }
}

class NewsViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}