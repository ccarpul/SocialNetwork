package com.example.socialnetwork.utils

object Constants {

    //NETWORK
    const val BASE_URL_INSTAGRAM = "https://graph.instagram.com/"
    const val ENDPOINT_MEDIA = "me/media?fields=id,caption,username,media_url&limit=4"
    const val ENDPOINT_ME = "me?fields=username,media_count&access_token="
    const val ACCESS_TOKEN = "IGQVJVWGNWbGFPM0RreDRWWE1mTjE2Q0VKVDhuMjVDc0dSTDk1UTJmOGRHWUhtOXFHWDhL" +
            "Ri00ZAEprOG5nTS1uNktoZAEJ5S3ktcDVSNmJKdWU3OEF1TzJYV09VWWlNUno0bEJoSU5NWEllemxOQUsxUQZDZD"

    const val LOGIN_URL_AUTH_INSTAGRAM="https://api.instagram.com/oauth/authorize?"
    const val REQUEST_ACCESS_TOKEN_URL = "https://api.instagram.com/oauth/access_token?"
    const val ID_APP = "1606032696237885"
    const val SECRET_KEY_INSTAGRAM = "ac0e9ade87fdf49a5bad365a268aa434"
    const val URI_REDIRECT = "https://bitbucket.org/CarPuL/"
    const val GRANTYPE = "authorization_code"

    const val URL_INSTAGRAM_AUTH = LOGIN_URL_AUTH_INSTAGRAM+
            "&client_id=$ID_APP" +
            "&redirect_uri=$URI_REDIRECT" +
            "&scope=user_profile,user_media" +
            "&response_type=code"

    //Twitter
    const val CONSUMER_KEY = "Xh59V6pto5EzFmOQdrq8Qj9tu"
    const val CONSUMER_SECRET = "uFfhSFJFS2ADnDpb1b9HiwDRBVoXCzX6fHKGtoMF0LFMGZUA5P"
    const val ENDPOINT_TWITTER = "1.1/statuses/home_timeline.json"
}



