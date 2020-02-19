package com.zsk.androtweet.ui.fragments

import android.os.Bundle
import com.kaloglu.library.ui.BaseFragment
import com.zsk.androtweet.R

class TweetListFragment : BaseFragment(R.layout.tweet_list_fragment) {

    companion object {
        fun newInstance() = TweetListFragment()
    }

    override fun initUserInterface(savedInstanceState: Bundle?) {

    }

}
