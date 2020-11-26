package com.copincomics.copinapp.data

data class listProduct(
    val head: HeaderContext,
    val body: BodylistProduct
)

data class BodylistProduct(
    val coins: String,
    val list: List<ProductItem>
)

data class ProductItem(
    val amount: String,
    val bouns: String,
    val price: String,
    val coinname: String
)

