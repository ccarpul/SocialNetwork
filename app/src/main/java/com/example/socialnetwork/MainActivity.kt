package com.example.socialnetwork

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makeNavigationViewDrawer()

        val navController = findNavController(R.id.navHostFragment)
        navDrawer.setupWithNavController(navController)
    }

    private fun makeNavigationViewDrawer() {
        val iconMenuNavigation = ContextCompat.getDrawable(applicationContext, R.drawable.ic_hamburger_24)
        val drawer: DrawerLayout = findViewById(R.id.drawer)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolBar, R.string.app_name, R.string.app_name)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        toolBar.navigationIcon = iconMenuNavigation
    }
}