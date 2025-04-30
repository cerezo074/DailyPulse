package com.eli.examples.dailypulse.articles.di

import com.eli.examples.dailypulse.articles.presentation.ArticlesViewModel
import com.eli.examples.dailypulse.articles.services.network.ArticlesRemoteDataService
import com.eli.examples.dailypulse.articles.services.persistence.ArticlesDataSource
import com.eli.examples.dailypulse.articles.services.repository.ArticlesRepository
import com.eli.examples.dailypulse.articles.use_cases.ListArticleUseCase
import org.koin.dsl.module

val articlesModule = module {
    single<ArticlesRemoteDataService> { ArticlesRemoteDataService(get()) }
    single<ArticlesDataSource> { ArticlesDataSource(get()) }
    single<ArticlesRepository> { ArticlesRepository(get(), get()) }
    single<ListArticleUseCase> { ListArticleUseCase(get()) }
    single<ArticlesViewModel> { ArticlesViewModel(get()) }
}

