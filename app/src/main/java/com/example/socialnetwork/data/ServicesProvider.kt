package com.example.socialnetwork.data

import com.example.socialnetwork.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun getApiServiceInstagram(): InstagramApiClient {
    return Retrofit.Builder()
        .baseUrl(Constants.BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getOkHttpClient())
        .build().run {
            create(InstagramApiClient::class.java)
        }
}

fun getOkHttpClient(): OkHttpClient {

    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder().addInterceptor(logging).build()
}

fun getApiServiceTwitter(): TwitterApiClient {
    return Retrofit.Builder()
        .baseUrl("https://api.twitter.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(getOkHttpClientTwitter())
        .build().run {
            create(TwitterApiClient::class.java)
        }
}

fun getOkHttpClientTwitter(): OkHttpClient {

    val interceptor = Interceptor { chain ->

        val request = chain.request().newBuilder()
            .url("https://api.twitter.com/1.1/statuses/home_timeline.json")
            .method("GET", null)
            //.addHeader("Authorization", Constants.AUTHORIZATION)
        val actualRequest = request.build()
        chain.proceed(actualRequest)
    }

    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.HEADERS
    return OkHttpClient.Builder().addInterceptor(logging).addInterceptor(interceptor).build()
}
