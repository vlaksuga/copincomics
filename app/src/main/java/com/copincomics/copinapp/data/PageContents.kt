package com.copincomics.copinapp.data

data class PageContents(
    val head: HeaderContext,
    val body: BodyPageContents
)

data class BodyPageContents(
    val banner: List<Banner>,
    val list: List<WebtoonItem>
)


