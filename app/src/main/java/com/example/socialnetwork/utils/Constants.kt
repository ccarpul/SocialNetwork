package com.example.socialnetwork.utils

object Constants {

    //NETWORK
    const val BASE_URL_INSTAGRAM = "https://graph.instagram.com/"
    const val ENDPOINT_MEDIA = "me/media?fields=id,caption,username,media_url&limit=4&access_token="
    const val ENDPOINT_ME = "me?fields=username,media_count&access_token="
    const val APIKEY = "IGQVJVWGNWbGFPM0RreDRWWE1mTjE2Q0VKVDhuMjVDc0dSTDk1UTJmOGRHWUhtOXFHWDhL" +
            "Ri00ZAEprOG5nTS1uNktoZAEJ5S3ktcDVSNmJKdWU3OEF1TzJYV09VWWlNUno0bEJoSU5NWEllemxOQUsxUQZDZD"

    //Twitter
    const val CONSUMER_KEY = "Xh59V6pto5EzFmOQdrq8Qj9tu"
    const val CONSUMER_SECRET = "uFfhSFJFS2ADnDpb1b9HiwDRBVoXCzX6fHKGtoMF0LFMGZUA5P"
    const val ENDPOINT_TWITTER = "1.1/statuses/home_timeline.json"
}