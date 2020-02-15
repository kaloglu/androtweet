package com.zsk.androtweet.ui.activities

import android.content.Intent
import android.os.Handler
import com.kaloglu.library.BaseActivity
import com.zsk.androtweet.R

class SplashScreenActivity : BaseActivity() {
    override val contentResourceId = R.layout.activity_splash_screen
    private var splashHandler = Handler()

    override fun initUserInterface() {
        startActivityAfterDelay(3000)
    }

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
