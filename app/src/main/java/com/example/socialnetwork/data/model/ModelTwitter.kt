package com.example.socialnetwork.data.model

class ModelTwitter : ArrayList<ModelTwitter.ModelTwitterItem>() {
    data class ModelTwitterItem(
        val retwit_count: Int,
        val text: String,
        val user: User
    ) {
        data class User(
            val description: String,
            val entities: Entities,
            val favourites_count: Int,
            val location: String,
            val profile_image_url_https: String,
            val name: String,
            val screen_name: String,
            val url: String,
            val verified: Boolean
        ) {
            data class Entities(
                val media: Media
            ) {
                data class Media(
                    val media_url_https: String
                )
            }
        }
    }
}