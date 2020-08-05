package com.example.socialnetwork.data.model

data class ModelResponse(
    val data: List<Data>,
    val paging: Paging
)
data class Data(
    val caption: String,
    val id: String,
    val media_url: String,
    val username: String
)
data class Paging(
    val cursors: Cursors,
    val next: String
)

data class Cursors(
    val after: String,
    val before: String
)