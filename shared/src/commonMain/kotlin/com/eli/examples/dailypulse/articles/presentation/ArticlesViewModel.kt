package com.eli.examples.dailypulse.articles.presentation

import com.eli.examples.dailypulse.articles.use_cases.ListArticleUseCase
import com.eli.examples.dailypulse.utils.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticlesViewModel(
    private val listArticleUseCase: ListArticleUseCase
) : BaseViewModel() {

    private val internalArticlesState: MutableStateFlow<ArticlesState> = MutableStateFlow(
        ArticlesState(loading = true)
    )

    val articlesState: StateFlow<ArticlesState>
        get() {
            return internalArticlesState
        }

    init {
        getArticles()
    }

    private fun getArticles() {
        scope.launch {
            val fetchedArticles = listArticleUseCase.getArticles()
            internalArticlesState.emit(ArticlesState(articles = fetchedArticles))
        }
    }
}