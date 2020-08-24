package com.example.socialnetwork.data

import com.example.socialnetwork.utils.Constants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import twitter4j.Paging
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder


fun getApiServiceInstagram(): InstagramApiClient {
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL_INSTAGRAM)
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

fun getOkHttpClientAccessToken(): OkHttpClient {

    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder().addInterceptor(logging).build()
}


fun getAccessTokenInstagram(): AccessTokenClient{
    return Retrofit.Builder()
        .baseUrl("https://api.instagram.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(getOkHttpClientAccessToken())
        .build().run {
            create(AccessTokenClient::class.java)
        }
}
