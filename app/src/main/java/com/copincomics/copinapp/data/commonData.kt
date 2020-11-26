package com.copincomics.copinapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class WebtoonItem(
    val salerentcnt: String,
    val canpurchase: String,
    val systemtag: List<Tag>,
    val isending: String,
    val waitterm: String,
    val rate: String,
    val price: String,
    val salecnt: String,
    val canrent: String,
    val termgiftcnt: String,
    val titlename: String,
    val pubdate: String,
    val titleid: String,
    val tempfreecnt: String,
    val cantermgift: String,
    val tags: List<String>,
    val isup: String,
    val isnew: String,
    val viewcnt: String,
    val titlepkey: String,
    val short: String,
    val lastupdate: String,
    val comic: String,
    val status: String,
    val rentprice: String,
    val authors: List<Authors>,
    val epicnt: String,
    val freecnt: String,
    val thumbs: Thumbs,
)


data class WebtoonRanking(
    val salerentcnt: String,
    val canpurchase: String,
    val systemtag: List<Tag>,
    val isnew: String,
    val isending: String,
    val waitterm: String,
    val rate: String,
    val price: String,
    val salecnt: String,
    val rank: String,
    val canrent: String,
    val termgiftcnt: String,
    val titlename: String,
    val titleid: String,
    val tempfreecnt: String,
    val rankdt: String,
    val cantermgift: String,
    val tags: List<String>,
    val viewcnt: String,
    val titlepkey: String,
    val short: String,
    val lastupdate: String,
    val comic: String,
    val status: String,
    val rentprice: String,
    val authors: List<Authors>,
    val epicnt: String,
    val freecnt: String,
    val thumbs: Thumbs
)


data class WebtoonGroup(
    val bgcolor1: String,
    val groupname2: String,
    val rate: String,
    val groupkind: String,
    val bgcolor2: String,
    val collagepkey: String,
    val groupflag: String,
    val titlepkey: String,
    val thumbs: Thumbs,
    val groupname: String,
    val status: String
)

data class HeaderContext(
    val status: String,
    val msg: String
)


data class Thumbs(
    val thumb1x1: String,
    val thumb3x4: String,
    val thumb5x3: String,
    val thumb2x1: String,
    val thumb3x2: String,
    val thumb4x3: String
)


data class Authors(
    val authorpkey: String,
    val titlepkey: String,
    val authorname: String,
    val aswith: String,
    val status: String
)


data class Banner(
    val img: String,
    val is19: String,
    val link: String
)

@Parcelize
data class Tag(
    val tagkind: String,
    val name: String
) : Parcelable