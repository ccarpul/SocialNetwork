package com.example.socialnetwork.ui.login

import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

class LoginRepository {

    fun setLoginByTwitter(activity: FragmentActivity?): Task<AuthResult> {
        val instance: FirebaseAuth = FirebaseAuth.getInstance()
        val pendingAuthResult = instance.pendingAuthResult
        return if(pendingAuthResult != null){
            pendingAuthResult
        }else{

            val oAuthProvider = OAuthProvider.newBuilder("twitter.com")
            instance.startActivityForSignInWithProvider(activity!!, oAuthProvider.build())
                .addOnSuccessListener {

            }
        }
    }
}