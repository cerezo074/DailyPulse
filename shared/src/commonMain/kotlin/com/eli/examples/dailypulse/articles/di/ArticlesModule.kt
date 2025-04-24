package com.eli.examples.dailypulse.articles.di

import com.eli.examples.dailypulse.articles.presentation.ArticlesViewModel
import com.eli.examples.dailypulse.articles.services.network.ArticlesService
import com.eli.examples.dailypulse.articles.use_cases.ListArticleUseCase
import org.koin.dsl.module

val articlesModule = module {
    single<ArticlesService> { ArticlesService(get()) }
    single<ListArticleUseCase> { ListArticleUseCase(get()) }
    single<ArticlesViewModel> { ArticlesViewModel(get()) }
}

