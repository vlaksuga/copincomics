package com.copincomics.copinapp.data

data class Libraryhistory(
    val head: HeaderContext,
    val body: BodyLibraryhistory
)


data class BodyLibraryhistory(
    val list: List<LibraryHistoryItem>
)


data class LibraryHistoryItem(
    val accountpkey: String,
    val amount: String,
    val relate: String,
    val kind: String,
    val accountlibraryhistorypkey: String,
    val titlepkey: String,
    val episodepkey: String,
    val issuedate: String,
)