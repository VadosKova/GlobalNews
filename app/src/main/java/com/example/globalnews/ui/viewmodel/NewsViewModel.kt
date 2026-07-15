package com.example.globalnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.globalnews.domain.model.Article
import com.example.globalnews.domain.usecase.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val useCases: NewsUseCases
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortBy = MutableStateFlow("publishedAt")
    val sortBy: StateFlow<String> = _sortBy.asStateFlow()

    private val _selectedCategory = MutableStateFlow("technology")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val searchArticles: Flow<List<Article>> = useCases.getSearchArticles()
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
            useCases.refreshSearchNews(query = _searchQuery.value.trim(), page = page)
            isLoading.value = false
        }
    }

    fun loadCategoryPage(page: Int) {
        _categoryCurrentPage.value = page
        viewModelScope.launch {
            isLoading.value = true
            useCases.refreshCategoryNews(category = _selectedCategory.value, page = page)
            isLoading.value = false
        }
    }

    fun getArticlesByCategory(category: String): Flow<List<Article>> {
        return _sortBy.flatMapLatest { sortOrder ->
            useCases.getSortedCategoryArticles(category, sortOrder)
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