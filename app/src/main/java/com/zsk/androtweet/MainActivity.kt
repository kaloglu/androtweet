package com.zsk.androtweet

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.kaloglu.library.BaseActivity
import com.kaloglu.library.setup
import com.zsk.androtweet.adapters.Commons
import com.zsk.androtweet.adapters.TimelineAdapter
import com.zsk.androtweet.models.TimelineDao
import com.zsk.androtweet.repositories.TimelineRepositoryImpl
import com.zsk.androtweet.usecases.GetTweetsUseCasesImpl
import com.zsk.androtweet.usecases.InsertTweetsUseCasesImpl
import com.zsk.androtweet.usecases.RemoveTweetsUseCasesImpl
import com.zsk.androtweet.viewmodels.TimelineViewModel
import com.zsk.androtweet.viewmodels.TimelineViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

//
// Created by  on 2020-02-02.
//
class MainActivity : BaseActivity() {
    private val timelineFactory = TimelineViewModelFactory(
            GetTweetsUseCasesImpl(TimelineRepositoryImpl()),
            RemoveTweetsUseCasesImpl(TimelineRepositoryImpl()),
            InsertTweetsUseCasesImpl(TimelineRepositoryImpl())
    )
    lateinit var viewModel: TimelineViewModel

    private val timelineAdapter by lazy { TimelineAdapter() }
    private lateinit var timelineDao: TimelineDao
    private var rewardedAd: RewardedAd? = null
    private var mInterstitialAd: InterstitialAd? = null

    override val contentResourceId: Int
        get() = R.layout.activity_main

    override fun initUserInterface() {

        viewModel = ViewModelProvider(this, timelineFactory).get(TimelineViewModel::class.java)

        tweetsRecyclerView.setup(timelineAdapter)

        timelineDao = AndroTweetApp.database.timelineDao()
        timelineDao.timeline.observe(this, Observer {
            timelineAdapter.items = it
        })
        Commons.isLogon(this)
        initAds()
        Commons.GetTweets(this).execute()

        chk_SelectAll.setOnCheckedChangeListener { _, isChecked ->
            timelineAdapter.items = timelineAdapter.items.map {
                it.isSelected = isChecked
                return@map it
            }
            timelineAdapter.notifyDataSetChanged()
        }
    }

    private fun showAds() {
        rewardedAd?.show(this, object : RewardedAdCallback() {
            override fun onRewardedAdFailedToShow(i: Int) {
                super.onRewardedAdFailedToShow(i)
                mInterstitialAd?.show()
            }

            override fun onRewardedAdClosed() {
                Log.e("REWARDED_VIDEO", "VIDEO_CLOSED!")
                createAndLoadRewardedAd()
            }

            override fun onUserEarnedReward(rewardItem: RewardItem) {
                Log.e("REWARDED_VIDEO", "REWARD!")
            }
        })
    }

    fun doThings(paramView: View) {
        if (!BuildConfig.DEBUG)
            showAds()
        when (paramView.id) {
            R.id.deleteTweet -> {
                Commons.deleteSelected(this, timelineAdapter.items.filter { it.isSelected })
            } /*Commons.deleteSelected(this, adapter)*/
            R.id.refreshTweet -> {
                Commons.GetTweets(this).execute(true)
            } /*Commons.refreshTweetList(this, tweetList_on_home)*/
            R.id.logOut -> Commons.logOut(this)
            else -> {
            }
        }
    }

    private fun initAds() {
        adViewBanner.loadAd(AdRequest.Builder().build())
        MobileAds.initialize(this) { createAndLoadRewardedAd() }
    }

    fun createAndLoadRewardedAd() {
        rewardedAd = RewardedAd(this, resources.getString(R.string.rewarded_ad_unit_id))
        val adLoadCallback: RewardedAdLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                Log.e("REWARDED_VIDEO", "LOADED!")
            }

            override fun onRewardedAdFailedToLoad(errorCode: Int) {
                Log.e("REWARDED_VIDEO", "FAILED!")
                createAndLoadInterstitialAd()
            }
        }
        rewardedAd?.loadAd(AdRequest.Builder().build(), adLoadCallback)
    }

    fun createAndLoadInterstitialAd() {
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd?.let {

            it.adUnitId = getString(R.string.interstitial_ad_unit_id)
            it.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.e("INTERSTITIAL_AD", "LOADED!")
                }

                override fun onAdFailedToLoad(i: Int) {
                    super.onAdFailedToLoad(i)
                    Log.e("INTERSTITIAL_AD", "FAILED!")
                    createAndLoadRewardedAd()
                }

                override fun onAdClosed() { // Load the next interstitial.
                    createAndLoadRewardedAd()
                }
            }
            it.loadAd(AdRequest.Builder().build())
        }
    }
}