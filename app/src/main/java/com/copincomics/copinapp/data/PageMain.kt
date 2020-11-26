package com.copincomics.copinapp.data

data class PageMain(
    val head: HeaderContext,
    val body: PageMainBody
)

data class PageMainBody(
    val mainbanner: List<Banner>,
    val contentsrate: String,
    val listDayOfWeek: List<WebtoonItem>,
    val listRecent: List<WebtoonItem>,
    val listTermGift: List<WebtoonItem>,
    val listPDPick: List<PDPick>,
)


data class PDPick(
    val bgcolor1: String,
    val salerentcnt: String,
    val systemtag: List<Tag>,
    val bgcolor2: String,
    val collagepkey: String,
    val groupflag: String,
    val groupname: String,
    val tempfreecnt: String,
    val tags: List<String>,
    val sortno: String,
    val groupname2: String,
    val isup: String,
    val rate: String,
    val groupkind: String,
    val salecnt: String,
    val viewcnt: String,
    val titlepkey: String,
    val termgiftcnt: String,
    val lastupdate: String,
    val status: String,
    val authors: List<Authors>,
    val epicnt: String,
    val freecnt: String,
    val thumbs: Thumbs,
)