package com.example.socialnetwork.data

class prueba : ArrayList<prueba.pruebaItem>(){
    data class pruebaItem(
        val favorited: Boolean,
        val retweeted: Boolean,
        val text: String,
        val user: User
    ) {
        data class User(
            val description: String,
            val entities: Entities,
            val favourites_count: Int,
            val location: String,
            val profile_image_url_https: String,
            val screen_name: String,
            val url: String,
            val verified: Boolean
        ) {
            data class Entities(
                val url: Url
            ) {
                data class Url(
                    val urls: List<Url>
                ) {
                    data class Url(
                        val display_url: String,
                        val expanded_url: String,
                        val url: String
                    )
                }
            }
        }
    }
}