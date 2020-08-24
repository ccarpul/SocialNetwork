package com.example.socialnetwork

import android.content.res.ColorStateList
import android.os.Bundle
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
        setSupportActionBar(toolbar)

        makeNavigationViewDrawer()

        val navController = findNavController(R.id.navHostFragment)
        navDrawer.setupWithNavController(navController)
    }

    private fun makeNavigationViewDrawer() {
        val iconMenuNavigation =
            ContextCompat.getDrawable(this, R.drawable.ic_hamburger_24)
        val drawer: DrawerLayout = findViewById(R.id.drawer)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.app_name, R.string.app_name
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        toolbar.navigationIcon = iconMenuNavigation
        navDrawer.apply {
            setBackgroundColor(resources.getColor(R.color.white))
            itemTextColor = ColorStateList.valueOf(resources.getColor(R.color.black))
            itemIconTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
        }

    }
}