package com.copincomics.copinapp.data

data class NoticeList(
    val head: HeaderContext,
    val body: BodyNoticeList
)

data class BodyNoticeList(
    val list: List<NoticeItem>
)

data class NoticeItem(
    val noticetitle: String,
    val noticepkey: String,
    val noticetxt: String,
    val pubdate: String,
    val status: String
)


//"noticetitle": "a2",
//"noticepkey": "3",
//"noticetxt": "b2",
//"pubdate": "20201029075116",
//"status": "NORMAL"

