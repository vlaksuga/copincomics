package com.copincomics.copinapp.apis

import android.content.SharedPreferences
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

class RequestFactory(private val sharedPreferences: SharedPreferences) {

    private class AuthInterceptor(private val t: String, private val c: String, val d: String, val v: String) :
        Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val url: HttpUrl = chain.request().url()
                .newBuilder()
                .addQueryParameter("t", t)
                .addQueryParameter("c", c)
                .addQueryParameter("d", d)
                .addQueryParameter("v", v)
                .build()

            val request: Request = chain.request().newBuilder().url(url).build()
            return chain.proceed(request)
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.NONE)

        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(5, TimeUnit.MINUTES)
        httpClient.readTimeout(5, TimeUnit.MINUTES)

        val t = sharedPreferences.getString("t", " ")!!
        val c = sharedPreferences.getString("deviceId", "")!!
        val d = "android"
        val v = sharedPreferences.getString("appVersion", "1.0")!!

        httpClient.addInterceptor(AuthInterceptor(t, c, d, v))

        return httpClient.build()
    }

    private val client = createOkHttpClient()

    val dispatcher: Dispatcher = client.dispatcher()

    val serviceAPIs = Retrofit.Builder()
        .baseUrl("https://dapi.copincomics.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build().create(ServiceAPIs::class.java)

    //
    val accountAPIs = Retrofit.Builder()
        .baseUrl("https://dapi.copincomics.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build().create(AccountAPIs::class.java)

    val myAPIs = Retrofit.Builder()
        .baseUrl("https://dapi.copincomics.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build().create(MyAPIs::class.java)


    val promotion = Retrofit.Builder()
        .baseUrl("https://dapi.copincomics.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build().create(PromotionAPIs::class.java)


    val pay = Retrofit.Builder()
        .baseUrl("https://dapi.copincomics.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build().create(PaymentAPIs::class.java)


    val customerService = Retrofit.Builder()
        .baseUrl("https://dapi.copincomics.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build().create(CustomerServiceAPIs::class.java)


    val notice = Retrofit.Builder()
        .baseUrl("https://dapi.copincomics.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build().create(NoticeAPIs::class.java)


    val searchAPIs = Retrofit.Builder()
        .baseUrl("https://dapi.copincomics.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build().create(SearchAPIs::class.java)


}









