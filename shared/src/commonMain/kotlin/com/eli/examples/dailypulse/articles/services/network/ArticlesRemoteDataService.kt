package com.eli.examples.dailypulse.articles.services.network

import com.eli.examples.dailypulse.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

data class ArticlesConfiguration(
    val countryCode: String,
    val businessCategory: String,
    val apiKey: String
) {
    companion object {
        val DEFAULT_CONFIG = ArticlesConfiguration(
            "us",
            "business",
            BuildKonfig.NEWS_API_KEY
        )
    }
}

class ArticlesRemoteDataService(
    private val httpClient: HttpClient,
    private val configuration: ArticlesConfiguration = ArticlesConfiguration.DEFAULT_CONFIG
) {

    private val allArticlesURL: String
        get() {
            return "https://newsapi.org/v2/top-headlines?country=${configuration.countryCode}&" +
                    "category=${configuration.businessCategory}&" +
                    "apiKey=${configuration.apiKey}"
        }

    suspend fun fetchArticles(): List<ArticleRemoteItem> {
        val response: ArticlesResponse = httpClient.get(allArticlesURL).body()
        return response.articles
    }
}