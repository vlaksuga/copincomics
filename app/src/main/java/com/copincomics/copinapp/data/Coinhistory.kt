package com.copincomics.copinapp.data

data class Coinhistory(
    val head: HeaderContext,
    val body: BodyCoinhistory
)


data class BodyCoinhistory(
    val list: List<CoinHistoryItem>
)


data class CoinHistoryItem(
    val accountpkey: String,
    val txt: String,
    val amount: String,
    val coinhistorypkey: String,
    val issuedate: String,
    val historykind: String,
    val transactionid: String,
)