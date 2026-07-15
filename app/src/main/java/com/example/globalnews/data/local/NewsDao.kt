package com.example.globalnews.data.local

import androidx.room.*
import com.example.globalnews.data.local.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articles_table WHERE category = 'search'")
    fun getSearchArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles_table WHERE category = :categoryName")
    fun getArticlesByCategory(categoryName: String): Flow<List<ArticleEntity>>

    @Query("DELETE FROM articles_table WHERE category = :categoryName")
    suspend fun clearArticlesByCategory(categoryName: String)
}