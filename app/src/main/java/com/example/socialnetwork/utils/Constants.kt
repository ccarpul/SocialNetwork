package com.example.socialnetwork.utils

object Constants {

    //INSTAGRAM
    const val BASE_URL_INSTAGRAM = "https://graph.instagram.com/"
    const val ENDPOINT_MEDIA     = "me/media?fields=id,caption,username,media_url&limit=4"
    const val ENDPOINT_ME        = "me?fields=username,media_count&access_token="

    //ACCESS TOKEN INSTAGRAM
    const val URL_AUTH_INSTAGRAM        = "https://api.instagram.com/oauth/authorize?"
    const val ENDPOINT_ACCESS_TOKEN     = "oauth/access_token?"
    const val CONSUMER_ID_INSTAGRAM     = "1606032696237885"
    const val CONSUMER_SECRET_INSTAGRAM = "ac0e9ade87fdf49a5bad365a268aa434"
    const val URI_REDIRECT              = "https://bitbucket.org/CarPuL/"
    const val GRANTYPE                  = "authorization_code"

    const val URL_INSTAGRAM_AUTH = URL_AUTH_INSTAGRAM+
            "&client_id=$CONSUMER_ID_INSTAGRAM" +
            "&redirect_uri=$URI_REDIRECT" +
            "&scope=user_profile,user_media" +
            "&response_type=code"

    //Twitter
    const val CONSUMER_KEY = "Xh59V6pto5EzFmOQdrq8Qj9tu"
    const val CONSUMER_SECRET = "uFfhSFJFS2ADnDpb1b9HiwDRBVoXCzX6fHKGtoMF0LFMGZUA5P"
}



