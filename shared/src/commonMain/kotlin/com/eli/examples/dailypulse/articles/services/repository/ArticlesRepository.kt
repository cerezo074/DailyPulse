package com.eli.examples.dailypulse.articles.services.repository

import com.eli.examples.dailypulse.articles.services.model.ArticleModel
import com.eli.examples.dailypulse.articles.services.network.ArticlesRemoteDataService
import com.eli.examples.dailypulse.articles.services.persistence.ArticlesDataSource
import com.eli.examples.dailypulse.db.DBArticleItem

class ArticlesRepository(
    private val dataSource: ArticlesDataSource,
    private val remoteDataService: ArticlesRemoteDataService
) {
    suspend fun getArticles(): List<ArticleModel> {
        val DBArticles = dataSource.getAllArticles()
        println("Got ${DBArticles.size} from the database!!")

        if (DBArticles.isEmpty()) {
            val remoteArticles = remoteDataService.fetchArticles()
            val resultArticles = mutableListOf<ArticleModel>()
            val dbArticles = mutableListOf<DBArticleItem>()

            for (article in remoteArticles) {
                dbArticles.add(DBArticleItem(article.title, article.desc, article.date, article.imageURL))
                resultArticles.add(ArticleModel(article.title, article.desc, article.date, article.imageURL))
            }

            dataSource.insertArticles(dbArticles)
            return resultArticles
        }

        return DBArticles.map { ArticleModel(it.title, it.desc, it.date, it.imageURL) }
    }
}