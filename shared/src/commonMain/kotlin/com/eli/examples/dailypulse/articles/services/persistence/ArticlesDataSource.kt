package com.eli.examples.dailypulse.articles.services.persistence

import com.eli.examples.dailypulse.db.DBArticleItem
import com.eli.examples.dailypulse.db.DailyPulseDatabase

class ArticlesDataSource(private val database: DailyPulseDatabase) {
    fun getAllArticles(): List<DBArticleItem> =
        database.dailyPulseDatabaseQueries.getAllArticles().executeAsList()

    fun insertArticles(articles: List<DBArticleItem>) {
        database.dailyPulseDatabaseQueries.transaction {
            articles.forEach { article ->
                insert(article)
            }
        }
    }

    fun clearArticles() = database.dailyPulseDatabaseQueries.removeAllArticles()

    private fun insert(article: DBArticleItem) {
        database.dailyPulseDatabaseQueries.insertArticle(
            article.title,
            article.desc,
            article.date,
            article.imageURL
        )
    }
}