package com.example.socialnetwork

import android.app.Application
import com.example.socialnetwork.di.DependenciesModuleInstagram
import com.example.socialnetwork.di.DependenciesModuleLogin
import com.example.socialnetwork.di.DependenciesModuleTwitter
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SocialNetworkApp: Application() {
    companion object{
        lateinit var mApp: SocialNetworkApp
            private set
    }
    override fun onCreate(){
        super.onCreate()
        mApp = this
        startKoin {
            androidContext(this@SocialNetworkApp)
            modules(listOf(DependenciesModuleTwitter, DependenciesModuleInstagram, DependenciesModuleLogin))
        }
    }

}