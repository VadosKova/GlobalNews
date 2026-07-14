package com.example.globalnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.globalnews.data.local.NewsDatabase
import com.example.globalnews.data.remote.RetrofitClient
import com.example.globalnews.data.repository.NewsRepository
import com.example.globalnews.ui.navigation.NavGraph
import com.example.globalnews.ui.navigation.Screen
import com.example.globalnews.ui.viewmodel.NewsViewModel
import com.example.globalnews.ui.viewmodel.NewsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = NewsDatabase.getDatabase(this)
        val repository = NewsRepository(RetrofitClient.apiService, database.newsDao())

        val viewModelFactory = NewsViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]

        setContent {
            val navController = rememberNavController()
            val items = listOf(Screen.Home, Screen.Category)

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route

                        items.forEach { screen ->
                            NavigationBarItem(
                                icon = { Icon(screen.icon, contentDescription = screen.title) },
                                label = { Text(screen.title) },
                                selected = currentRoute == screen.route,
                                onClick = {
                                    if (currentRoute != screen.route) {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
                    NavGraph(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}