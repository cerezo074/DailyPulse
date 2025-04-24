package com.eli.examples.dailypulse.articles.use_cases

import com.eli.examples.dailypulse.articles.presentation.Article
import com.eli.examples.dailypulse.articles.services.network.ArticleItem
import com.eli.examples.dailypulse.articles.services.network.ArticlesService
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.math.abs

class ListArticleUseCase(private val service: ArticlesService) {

    suspend fun getArticles(): List<Article> {
        val articleItems = service.fetchArticles()

        return resetInvalidAttributes(articleItems)
    }

    private fun resetInvalidAttributes(items: List<ArticleItem>): List<Article> =
        items.map { item ->
            Article(
                item.title,
                item.desc ?: "Click to find out more",
                getDaysAgoText(item.date),
                item.imageURL ?: "https://i.sstatic.net/areKx.png"
            )
        }

    private fun getDaysAgoText(date: String) : String {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val days = today.daysUntil(
            Instant.parse(date).toLocalDateTime(TimeZone.currentSystemDefault()).date
        )

        val result = when {
            abs(days) > 1 -> "${abs(days)} days ago"
            abs(days) == 1 -> "Yesterday"
            else -> "Today"
        }

        return result
    }
}