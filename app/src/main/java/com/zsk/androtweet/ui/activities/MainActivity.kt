package com.zsk.androtweet.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.google.android.material.navigation.NavigationView
import com.zsk.androtweet.R
import com.zsk.androtweet.mvi.LoginEvent
import com.zsk.androtweet.ui.activities.base.ATBaseActivity
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : ATBaseActivity(R.layout.main_activity), NavigationView.OnNavigationItemSelectedListener {

    private val appBarConfiguration by lazy {
        AppBarConfiguration(
                setOf(
                        R.id.tweetListFragment
                ), drawerLayout
        )
    }

    override fun initUserInterface(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, _, _ ->
            //TODO: if youu needed
        }

        navView.setNavigationItemSelectedListener(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activeFragment?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSupportNavigateUp() = navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_Profile -> showToast("not implemented!")

            R.id.action_Settings -> showToast("not implemented!")

            R.id.action_LogOut -> loginViewModel.postEvent(LoginEvent.LogOut)

            R.id.action_Home -> showToast("not implemented!")

            R.id.action_Mentions -> showToast("not implemented!")

            R.id.action_DM -> showToast("not implemented!")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun showToast(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
    }
}
