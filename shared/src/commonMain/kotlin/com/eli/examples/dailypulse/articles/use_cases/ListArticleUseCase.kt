package com.eli.examples.dailypulse.articles.use_cases

import com.eli.examples.dailypulse.articles.presentation.Article
import com.eli.examples.dailypulse.articles.services.network.ArticleItem
import com.eli.examples.dailypulse.articles.services.network.ArticlesService

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
                item.date,
                item.imageURL ?: "https://i.sstatic.net/areKx.png"
            )
        }
}