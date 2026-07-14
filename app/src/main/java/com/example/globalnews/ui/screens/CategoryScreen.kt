package com.example.globalnews.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.globalnews.data.model.Article
import com.example.globalnews.ui.viewmodel.NewsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(viewModel: NewsViewModel, onArticleClick: (Article) -> Unit) {
    val categories = listOf("technology", "business", "health", "sports", "science", "entertainment")
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val sortBy by viewModel.sortBy.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentPage by viewModel.categoryCurrentPage.collectAsState()

    val articles by viewModel.getArticlesByCategory(selectedCategory).collectAsStateWithLifecycle(initialValue = emptyList())
    var isFilterExpanded by remember { mutableStateOf(false) }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, selectedCategory) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadCategoryPage(currentPage)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(selectedCategory) {
        viewModel.loadCategoryPage(1)
    }

    val isLastPage = articles.size < 20

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Категорії",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.selectCategory(category) },
                    label = { Text(category.replaceFirstChar { it.uppercase() }) },
                    leadingIcon = {
                        val icon = when(category) {
                            "technology" -> Icons.Default.Build
                            "business" -> Icons.Default.Star
                            "health" -> Icons.Default.MedicalInformation
                            "sports" -> Icons.Default.Sports
                            "science" -> Icons.Default.Science
                            "entertainment" -> Icons.Default.Games
                            else -> Icons.AutoMirrored.Filled.List
                        }
                        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box {
            InputChip(
                selected = false,
                onClick = { isFilterExpanded = !isFilterExpanded },
                label = { Text("Фільтри: ${if(sortBy == "publishedAt") "Найновіші" else "Популярні"}") },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = isFilterExpanded,
                onDismissRequest = { isFilterExpanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                DropdownMenuItem(
                    text = { Text("Найновіші") },
                    onClick = {
                        viewModel.changeSortOrder("publishedAt")
                        isFilterExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Популярні") },
                    onClick = {
                        viewModel.changeSortOrder("popularity")
                        isFilterExpanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box (
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (articles.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Немає новин для цієї категорії", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = articles,
                        key =  { article -> article.url }
                    ) { article ->
                        SharedArticleItem(article = article, onArticleClick = onArticleClick)
                    }

                    item {
                        PaginationPanel(
                            currentPage = currentPage,
                            isLastPage = isLastPage,
                            onPageSelected = { page -> viewModel.loadCategoryPage(page) }
                        )
                    }
                }
            }
        }
    }
}