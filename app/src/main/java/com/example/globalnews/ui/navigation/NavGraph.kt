package com.example.globalnews.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.globalnews.data.model.Article
import com.example.globalnews.ui.screens.ArticleDetailScreen
import com.example.globalnews.ui.screens.CategoryScreen
import com.example.globalnews.ui.screens.HomeScreen
import com.example.globalnews.ui.viewmodel.NewsViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: NewsViewModel
) {
    var selectedArticle by remember { mutableStateOf<Article?>(null) }

    if (selectedArticle != null) {
        ArticleDetailScreen(
            article = selectedArticle!!,
            onBackClick = { selectedArticle = null }
        )
    }
    else {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onArticleClick = { article -> selectedArticle = article }
                )
            }
            composable(Screen.Category.route) {
                CategoryScreen(
                    viewModel = viewModel,
                    onArticleClick = { article -> selectedArticle = article }
                )
            }
        }
    }
}