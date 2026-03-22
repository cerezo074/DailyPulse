package com.eli.examples.dailypulse.articles.presentation

import com.eli.examples.dailypulse.articles.use_cases.ListArticleUseCase
import com.eli.examples.dailypulse.utils.BaseViewModel
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArticlesViewModel(
    private val listArticleUseCase: ListArticleUseCase
) : BaseViewModel() {

    private val _contentState: MutableStateFlow<ArticlesState> = MutableStateFlow(ArticlesState(loading = true))
    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)

    @NativeCoroutinesState
    val contentState: StateFlow<ArticlesState> = _contentState

    @NativeCoroutinesState
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        scope.launch {
            val fetchedArticles = listArticleUseCase.getArticles(false)
            _contentState.emit(ArticlesState(articles = fetchedArticles, loading = false))
        }
    }

    fun onRefreshContent() {
        scope.launch {
            refreshContent()
        }
    }

    @NativeCoroutines
    suspend fun onRefreshContentAsync() {
        refreshContent()
    }

    private suspend fun refreshContent() {
        _isRefreshing.update { true }
        val fetchedArticles = listArticleUseCase.getArticles(true)
        _isRefreshing.update { false }
        _contentState.update { ArticlesState(articles = fetchedArticles) }
    }
}