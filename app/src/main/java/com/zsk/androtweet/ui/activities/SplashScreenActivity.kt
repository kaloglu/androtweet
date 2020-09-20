package com.zsk.androtweet.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.kaloglu.library.ui.BaseActivity
import com.zsk.androtweet.R
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SplashScreenActivity : BaseActivity(R.layout.activity_splash_screen) {
    private var splashHandler = Handler()
    override fun initUserInterface(savedInstanceState: Bundle?) {
        startActivityAfterDelay(3000)
    }

    @Suppress("SameParameterValue")
    private fun startActivityAfterDelay(delay: Long) {
        splashHandler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, delay)
    }

    companion object {
        private const val DURATION = 3000
    }
}
