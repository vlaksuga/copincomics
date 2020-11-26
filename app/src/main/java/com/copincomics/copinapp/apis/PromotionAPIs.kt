package com.copincomics.copinapp.apis

import com.copincomics.copinapp.data.EmptyReturn
import com.copincomics.copinapp.data.Gift
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface PromotionAPIs {

    @GET("s/list/promotionList.json")
    fun promotionList(
    ): Call<Gift>


    @GET("ticket/getTicket.json")
    fun getTicket(
        @Query("promotionpkey") promotionpkey: String,
    ): Call<EmptyReturn>


    @GET("ticket/getAllTicket.json")
    fun getAllTicket(
    ): Call<EmptyReturn>

}