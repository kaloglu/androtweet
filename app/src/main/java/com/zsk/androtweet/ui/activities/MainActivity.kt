package com.zsk.androtweet.ui.activities

import android.content.Intent
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.zsk.androtweet.R
import com.zsk.androtweet.ui.activities.base.ATBaseActivity
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class MainActivity : ATBaseActivity(R.layout.main_activity) {

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            activeFragment?.onActivityResult(requestCode, resultCode, data)
        }
    }
}
