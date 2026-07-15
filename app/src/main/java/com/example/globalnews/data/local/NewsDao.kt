package com.example.globalnews.data.local

import androidx.room.*
import com.example.globalnews.domain.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)

    @Query("SELECT * FROM articles_table WHERE category = 'search'")
    fun getSearchArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles_table WHERE category = :categoryName")
    fun getArticlesByCategory(categoryName: String): Flow<List<Article>>

    @Query("DELETE FROM articles_table WHERE category = :categoryName")
    suspend fun clearArticlesByCategory(categoryName: String)

    @Query("DELETE FROM articles_table")
    suspend fun clearAllArticles()
}