package com.copincomics.copinapp.apis

import com.copincomics.copinapp.data.EmptyReturn
import com.copincomics.copinapp.data.MyQuestions
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface CustomerServiceAPIs {

    @GET("/c/listMyQuestion.json")
    fun listMyQuestion(
    ): Call<MyQuestions>

    @GET("/c/askQuestion.json")
    fun askQuestion(
        @Query("kind") kind: String,
        @Query("title") title: String,
        @Query("txt") text: String,

        ): Call<EmptyReturn>


}