package com.eli.examples.dailypulse.android.di

import com.eli.examples.dailypulse.articles.presentation.ArticlesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { ArticlesViewModel(get()) }
}