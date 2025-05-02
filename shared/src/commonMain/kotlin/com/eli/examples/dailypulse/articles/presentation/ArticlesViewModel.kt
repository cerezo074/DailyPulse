package com.eli.examples.dailypulse.articles.presentation

import com.eli.examples.dailypulse.articles.use_cases.ListArticleUseCase
import com.eli.examples.dailypulse.utils.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticlesViewModel(
    private val listArticleUseCase: ListArticleUseCase
) : BaseViewModel() {

    private val internalContentState: MutableStateFlow<ArticlesState> = MutableStateFlow(ArticlesState(loading = true))
    private val internalIsRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val contentState: StateFlow<ArticlesState>
        get() {
            return internalContentState
        }

    val isRefreshing: StateFlow<Boolean>
        get() {
            return internalIsRefreshing
        }

    init {
        scope.launch {
            val fetchedArticles = listArticleUseCase.getArticles(false)
            internalContentState.emit(ArticlesState(articles = fetchedArticles, loading = false))
        }
    }

    fun onRefreshContent() {
        scope.launch {
            refreshContent()
        }
    }

    suspend fun onRefreshContentAsync() {
        refreshContent()
    }

    private suspend fun refreshContent() {
        internalIsRefreshing.emit(true)
        val fetchedArticles = listArticleUseCase.getArticles(true)
        internalIsRefreshing.emit(false)
        internalContentState.emit(ArticlesState(articles = fetchedArticles))
    }
}