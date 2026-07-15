package com.example.globalnews.di

import android.content.Context
import androidx.room.Room
import com.example.globalnews.data.local.NewsDao
import com.example.globalnews.data.local.NewsDatabase
import com.example.globalnews.data.remote.NewsApiService
import com.example.globalnews.data.repository.NewsRepositoryImpl
import com.example.globalnews.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    companion object {
        @Provides
        @Singleton
        fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
            return Room.databaseBuilder(
                context,
                NewsDatabase::class.java,
                "news_database"
            ).fallbackToDestructiveMigration(true)
                .build()
        }

        @Provides
        @Singleton
        fun provideNewsDao(database: NewsDatabase): NewsDao = database.newsDao()

        @Provides
        @Singleton
        fun provideNewsApiService(): NewsApiService {
            return Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApiService::class.java)
        }
    }
}