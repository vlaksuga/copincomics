package com.copincomics.copinapp.data

data class Gift(
    val head: HeaderContext,
    val body: BodyGift
)

data class BodyGift(
    val list: List<GiftItem>
)

data class GiftItem(
    val ticketcount: String,
    val enddate: String,
    val remaincnt: String,
    val titlepkey: String,
    val startdate: String,
    val promotionkind: String,
    val titlename: String,
    val promotionpkey: String,
    val thumbs: Thumbs,
    val tags: List<String>
)

