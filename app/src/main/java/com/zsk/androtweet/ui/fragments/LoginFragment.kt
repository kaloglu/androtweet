package com.zsk.androtweet.ui.fragments

import android.view.View
import com.kaloglu.library.BaseFragment
import com.zsk.androtweet.R

class LoginFragment : BaseFragment() {
    override val resourceLayoutId = R.layout.login_fragment

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun initUserInterface(rootView: View) {
        super.initUserInterface(rootView)
    }

    override fun refresh() {
    }

    override fun enterAnimation() {
    }

    override fun exitAnimation() {
    }

}
