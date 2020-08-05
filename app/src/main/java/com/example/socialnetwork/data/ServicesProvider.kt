package com.example.socialnetwork.data

import com.example.socialnetwork.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
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
            .addHeader("Authorization", "OAuth oauth_consumer_key=\"Xh59V6pto5EzFmOQdrq8Qj9tu\",oauth_token=\"108441936-pJJyjYy2aTmcZSusXw1bCGVluSRcR3a8RJApFpLd\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"1596595682\",oauth_nonce=\"fNpsmSRtZ5u\",oauth_version=\"1.0\",oauth_callback=\"https%3A%2F%2Fsocial-media-a5c4a.firebaseapp.com%2F__%2Fauth%2Fhandler\",oauth_signature=\"WxBpSabjAl55N0klGE2I1mgw6lU%3D\"")
            .addHeader("Cookie", "personalization_id=\"v1_b0MuhsaVKsanS45G5LvMfg==\"; guest_id=v1%3A159650255739017038; _twitter_sess=BAh7CSIKZmxhc2hJQzonQWN0aW9uQ29udHJvbGxlcjo6Rmxhc2g6OkZsYXNo%250ASGFzaHsABjoKQHVzZWR7ADoPY3JlYXRlZF9hdGwrCJy0ILdzAToMY3NyZl9p%250AZCIlMDBlZjE5MGE4M2I1MzBjMGI0MGQxN2M3NjFhZWMzNjQ6B2lkIiVhNGZi%250AODE1MTQ1M2FkOGNkZjBmNjlmY2YzNGM2NzJjMQ%253D%253D--30ab44fdc39221b16837829af84e4170597a827f; lang=es")
        val actualRequest = request.build()
        chain.proceed(actualRequest)
    }

    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.HEADERS
    return OkHttpClient.Builder().addInterceptor(logging).addInterceptor(interceptor).build()
    /*
    return HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.HEADERS
        OkHttpClient.Builder().addInterceptor(interceptor).build()
    }*/

}
