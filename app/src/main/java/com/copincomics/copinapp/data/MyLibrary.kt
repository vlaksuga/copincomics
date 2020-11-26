package com.copincomics.copinapp.data

data class MyLibrary(
    val head: HeaderContext,
    val body: BodyMyLibrary
)

data class BodyMyLibrary(
    val likelist: List<WebtoonItem>,
    val ownedlist: List<WebtoonItem>,
    val recentlist: List<WebtoonItem>,
)


