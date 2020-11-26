package com.copincomics.copinapp.apis

import com.copincomics.copinapp.data.SearchPageData
import com.copincomics.copinapp.data.SearchQueryData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface SearchAPIs {

    // search main
    @GET("q/t.json")
    fun searchPage(
    ): Call<SearchPageData>

    // search query
    @GET("q/a.json")
    fun searchQuery(
        @Query("q") query: String,
    ): Call<SearchQueryData>
}