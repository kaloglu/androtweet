package com.zsk.androtweet.ui.fragments

import android.view.View
import com.kaloglu.library.BaseFragment
import com.zsk.androtweet.R

class TweetListFragment : BaseFragment() {
    override val resourceLayoutId = R.layout.tweet_list_fragment

    companion object {
        fun newInstance() = TweetListFragment()
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
