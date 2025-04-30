package com.eli.examples.dailypulse.articles.services.model

data class ArticleModel(
    val title: String,
    val desc: String?,
    val date: String,
    var imageURL: String?
)
