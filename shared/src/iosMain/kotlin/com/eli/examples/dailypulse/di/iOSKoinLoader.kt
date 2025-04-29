package com.eli.examples.dailypulse.di

import com.eli.examples.dailypulse.articles.presentation.ArticlesViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

object iOSKoinLoader {
    fun init(){
        val appModules = sharedKoinModules

        startKoin {
            modules(appModules)
        }
    }
}

class ArticlesInjector: KoinComponent {
    val viewModel: ArticlesViewModel by inject()
}