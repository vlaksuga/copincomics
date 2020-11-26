package com.copincomics.copinapp.data

data class SearchQueryData(
    val head: SearchQueryHeader,
    val body: SearchQueryBody,
)

data class SearchQueryHeader(
    val status: String,
)

data class SearchQueryBody(
    val list: List<WebtoonItem>,
    val q: String,
    val searchmode: String,
)
