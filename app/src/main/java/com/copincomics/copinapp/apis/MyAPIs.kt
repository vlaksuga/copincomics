package com.copincomics.copinapp.apis

import com.copincomics.copinapp.data.Coinhistory
import com.copincomics.copinapp.data.EmptyReturn
import com.copincomics.copinapp.data.Libraryhistory
import com.copincomics.copinapp.data.MyLibrary
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface MyAPIs {

    @GET("my/library.json")
    fun library(
    ): Call<MyLibrary>


    @GET("a/changeProfile.json")
    fun changeProfile(
        @Query("profileimage") profileimage: String,
        @Query("nick") nick: String,
    ): Call<EmptyReturn>


    @GET("my/libraryhistory.json")
    fun libraryhistory(
    ): Call<Libraryhistory>


    @GET("my/coinhistory.json")
    fun coinhistory(
    ): Call<Coinhistory>


}