package com.example.socialnetwork.di

import com.example.socialnetwork.data.getAccessTokenInstagram
import com.example.socialnetwork.data.getApiServiceInstagram
import com.example.socialnetwork.ui.accesstoken.AccessTokenRepository
import com.example.socialnetwork.ui.accesstoken.AccessTokenViewModel
import com.example.socialnetwork.ui.instagram.InstagramRespository
import com.example.socialnetwork.ui.instagram.InstagramViewModel
import com.example.socialnetwork.ui.twitter.TwitterRepository
import com.example.socialnetwork.ui.twitter.TwitterViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val DependenciesModuleTwitter = module {
    viewModel { TwitterViewModel(get()) }
    single { TwitterRepository() }

}

val DependenciesModuleInstagram = module {
    viewModel { InstagramViewModel(get()) }
    single { InstagramRespository(get()) }
    single { getApiServiceInstagram() }
}
val DependenciesModuleAccessToken = module {
    viewModel { AccessTokenViewModel(get()) }
    single { AccessTokenRepository(get()) }
    single { getAccessTokenInstagram() }
}
