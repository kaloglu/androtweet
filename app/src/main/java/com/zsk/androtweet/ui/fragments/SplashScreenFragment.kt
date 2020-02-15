package com.zsk.androtweet.ui.fragments

import com.kaloglu.library.BaseFragment
import com.zsk.androtweet.R

class SplashScreenFragment : BaseFragment() {
    override val resourceLayoutId = R.layout.splash_screen_fragment

    companion object {
        fun newInstance() = SplashScreenFragment()
    }

    override fun refresh() {
    }

    override fun enterAnimation() {
    }

    override fun exitAnimation() {
    }

}
