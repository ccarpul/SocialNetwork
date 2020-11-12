package com.example.socialnetwork.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.socialnetwork.MainActivity
import com.example.socialnetwork.R
import kotlinx.android.synthetic.main.splash.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class Splash : AppCompatActivity() {

    lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        +View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN



        job = GlobalScope.launch(Dispatchers.Main) {
            val intent = Intent(this@Splash, MainActivity::class.java)
            var name = ""
            sendMessage("F\tr\to\tm\t", 100).collect {
                name += it
                from.text = name
            }
            delay(300)
            name = ""
            sendMessage(resources.getString(R.string.carpul_company), 50).collect {
                name += it
                nameCompany.text = name
            }
            delay(1_000)
            startActivity(intent)
            finish()
        }
    }

    private fun sendMessage(word: String, time: Long) = flow {
        word.forEach {
            delay(time)
            emit(it)
        }
    }



    override fun onDestroy() {
        super.onDestroy()
       job.cancel()
    }
}