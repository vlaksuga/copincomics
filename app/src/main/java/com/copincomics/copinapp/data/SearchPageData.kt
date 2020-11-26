package com.copincomics.copinapp.data

data class SearchPageData(
    val head: SearchHeader,
    val body: SearchBody,
)

data class SearchHeader(
    val status: String,
)

data class SearchBody(
    val taglist: List<SearchTag>,
)

data class SearchTag(
    val tag: String,
    val list: List<WebtoonItem>,
)