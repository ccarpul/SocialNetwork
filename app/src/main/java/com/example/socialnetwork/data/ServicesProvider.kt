package com.example.socialnetwork.data

import com.example.socialnetwork.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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
