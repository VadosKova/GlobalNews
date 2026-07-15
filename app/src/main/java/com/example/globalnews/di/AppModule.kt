package com.example.globalnews.di

import android.content.Context
import com.example.globalnews.data.local.NewsDao
import com.example.globalnews.data.local.NewsDatabase
import com.example.globalnews.data.remote.NewsApiService
import com.example.globalnews.data.remote.RetrofitClient
import com.example.globalnews.data.repository.NewsRepositoryImpl
import com.example.globalnews.domain.repository.NewsRepository
import com.example.globalnews.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return NewsDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideNewsDao(database: NewsDatabase): NewsDao = database.newsDao()

    @Provides
    @Singleton
    fun provideNewsApiService(): NewsApiService = RetrofitClient.apiService

    @Provides
    @Singleton
    fun provideNewsRepository(
        apiService: NewsApiService,
        newsDao: NewsDao
    ): NewsRepository {
        return NewsRepositoryImpl(apiService, newsDao)
    }

    @Provides
    @Singleton
    fun provideNewsUseCases(repository: NewsRepository): NewsUseCases {
        return NewsUseCases(
            getSearchArticles = GetSearchArticlesUseCase(repository),
            getSortedCategoryArticles = GetSortedCategoryArticlesUseCase(repository),
            refreshSearchNews = RefreshSearchNewsUseCase(repository),
            refreshCategoryNews = RefreshCategoryNewsUseCase(repository)
        )
    }
}