package com.copincomics.copinapp.apis

import com.copincomics.copinapp.data.NoticeList
import retrofit2.Call
import retrofit2.http.GET


interface NoticeAPIs {

    @GET("c/listNotice.json")
    fun listNotice(
    ): Call<NoticeList>

}