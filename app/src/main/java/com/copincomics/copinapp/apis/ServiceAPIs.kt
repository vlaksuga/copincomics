package com.copincomics.copinapp.apis

import com.copincomics.copinapp.data.PageContents
import com.copincomics.copinapp.data.PageMain
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ServiceAPIs {

    // home main
    @GET("s/pageMain.json")
    fun pageMain(
    ): Call<PageMain>

    // genre
    @GET("s/list/genre.json")
    fun genre(
        @Query("tag") tag: String,
    ): Call<PageContents>


    // week
    @GET("s/list/week.json")
    fun week(
        @Query("dayname") dayname: String,
    ): Call<PageContents>


    // tflist
    @GET("s/list/tflist.json")
    fun tflist(
    ): Call<PageContents>


}