package com.example.globalnews.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.globalnews.data.model.Article

@Composable
fun SharedArticleItem(article: Article, onArticleClick: (Article) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .clickable { onArticleClick(article) }
            .padding(8.dp)
    ) {
        article.urlToImage?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .height(96.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                val badgeText = if (article.category == "search") "General" else article.category
                Text(
                    text = badgeText.replaceFirstChar { it.uppercase() },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = article.title ?: "No title",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }

            article.publishedAt?.let { date ->
                val cleanDate = date.split("T").firstOrNull() ?: date
                Text(
                    text = cleanDate,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun PaginationPanel(
    currentPage: Int,
    totalPages: Int = 5,
    isLastPage: Boolean = false,
    onPageSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isLeftEnabled = currentPage > 1
        IconButton(
            onClick = { if (isLeftEnabled) onPageSelected(currentPage - 1) },
            enabled = isLeftEnabled
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Назад",
                tint = if (isLeftEnabled) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }

        for (page in 1..totalPages) {
            val isSelected = page == currentPage
            val isPageEnabled = page <= currentPage || !isLastPage

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable(enabled = isPageEnabled && !isSelected) { onPageSelected(page) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = page.toString(),
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = when {
                        isSelected -> Color.White
                        isPageEnabled -> MaterialTheme.colorScheme.onSurface
                        else -> Color.Gray
                    }
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }

        val isRightEnabled = currentPage < totalPages && !isLastPage
        IconButton(
            onClick = { if (isRightEnabled) onPageSelected(currentPage + 1) },
            enabled = isRightEnabled
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Вперед",
                tint = if (isRightEnabled) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
}