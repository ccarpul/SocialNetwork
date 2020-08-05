package com.example.socialnetwork

import retrofit2.http.Header


object Constants {

    //NETWORK
    const val BASEURL = "https://graph.instagram.com/"
    const val ENDPOINT_MEDIA = "me/media?fields=id,caption,username,media_url&limit=4&access_token="
    const val ENDPOINT_ME = "me?fields=username,media_count&access_token="
    const val APIKEY = "IGQVJVWGNWbGFPM0RreDRWWE1mTjE2Q0VKVDhuMjVDc0dSTDk1UTJmOGRHWUhtOXFHWDhL" +
            "Ri00ZAEprOG5nTS1uNktoZAEJ5S3ktcDVSNmJKdWU3OEF1TzJYV09VWWlNUno0bEJoSU5NWEllemxOQUsxUQZDZD"


    //Twitter
    const val AUTHORIZATION =  "OAuth oauth_consumer_key=\"Xh59V6pto5EzFmOQdrq8Qj9tu\",oauth_token=\"108441936-pJJyjYy2aTmcZSusXw1bCGVluSRcR3a8RJApFpLd\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"1596584180\",oauth_nonce=\"lqtDG2pNR57\",oauth_version=\"1.0\",oauth_callback=\"https%3A%2F%2Fsocial-media-a5c4a.firebaseapp.com%2F__%2Fauth%2Fhandler\",oauth_signature=\"5itiFj6QY3KvEwkELDYHpTd%2FaNY%3D\""

}