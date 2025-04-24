package com.eli.examples.dailypulse.di

import com.eli.examples.dailypulse.articles.di.articlesModule

val sharedKoinModules = listOf(
    articlesModule,
    networkModule
)