package com.eli.examples.dailypulse.articles.presentation

import com.eli.examples.dailypulse.articles.use_cases.ListArticleUseCase
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ArticlesViewModel(
    private val listArticleUseCase: ListArticleUseCase
) : ViewModel() {

    private val _contentState = MutableStateFlow(viewModelScope, ArticlesState(loading = true))
    private val _isRefreshing = MutableStateFlow(viewModelScope, false)

    @NativeCoroutinesState
    val contentState: StateFlow<ArticlesState> = _contentState

    @NativeCoroutinesState
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        viewModelScope.launch {
            val fetchedArticles = listArticleUseCase.getArticles(false)
            _contentState.update { (ArticlesState(articles = fetchedArticles, loading = false)) }
        }
    }

    fun onRefreshContent() {
        viewModelScope.launch {
            refreshContent(useSuspension = false)
        }
    }

    @NativeCoroutines
    suspend fun onRefreshContentAsync() {
        refreshContent(useSuspension = true)
    }

    private suspend fun refreshContent(useSuspension: Boolean) {
        if (!useSuspension) {
            _isRefreshing.update { true }
        }

        delay(5000)
        val fetchedArticles = listArticleUseCase.getArticles(true)

        if (!useSuspension) {
            _isRefreshing.update { false }
        }

        _contentState.update { ArticlesState(articles = fetchedArticles) }
    }
}