package com.eli.examples.dailypulse.articles.presentation

import com.eli.examples.dailypulse.articles.services.network.ArticlesService
import com.eli.examples.dailypulse.articles.use_cases.ListArticleUseCase
import com.eli.examples.dailypulse.utils.BaseViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ArticlesViewModel : BaseViewModel() {

    private val listArticleUseCase: ListArticleUseCase
    private val internalArticlesState: MutableStateFlow<ArticlesState> = MutableStateFlow(
        ArticlesState(loading = true)
    )

    val articlesState: StateFlow<ArticlesState>
        get() {
            return internalArticlesState
        }

    init {
        val httpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }

        val service = ArticlesService(httpClient)
        listArticleUseCase = ListArticleUseCase(service)
        getArticles()
    }

    private fun getArticles() {
        scope.launch {
            val fetchedArticles = listArticleUseCase.getArticles()
            internalArticlesState.emit(ArticlesState(articles = fetchedArticles))
        }
    }
}