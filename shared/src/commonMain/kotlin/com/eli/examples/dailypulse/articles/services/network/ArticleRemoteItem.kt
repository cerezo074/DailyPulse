package com.eli.examples.dailypulse.articles.services.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleRemoteItem(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val desc: String?,
    @SerialName("publishedAt")
    val date: String,
    @SerialName("urlToImage")
    val imageURL: String?
)
