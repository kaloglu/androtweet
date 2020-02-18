package com.zsk.androtweet.ui.activities

import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.kaloglu.library.ui.BaseActivity
import com.zsk.androtweet.R
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : BaseActivity(R.layout.main_activity) {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    private val appBarConfiguration by lazy {
        AppBarConfiguration(
                setOf(
                        R.id.tweetListFragment
                ), drawerLayout
        )
    }

    override fun initUserInterface() {
        setSupportActionBar(toolbar)
        setUpNavigation()

    }

    private fun setUpNavigation() {
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)  //1

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)  //2

        navController.addOnDestinationChangedListener { _, _, _ ->
            //TODO: if youu needed
        }

    }

}
