package com.zsk.androtweet

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.zsk.androtweet.database.DBModel
import com.zsk.androtweet.adapters.Commons
import com.zsk.androtweet.adapters.TweetAdapter
import com.zsk.androtweet.models.Search
import kotlinx.android.synthetic.main.home_timeline.*

class Main : Activity() {
    private lateinit var dbModel: DBModel
    private var adapter: TweetAdapter? = null
    private var context: Context? = null
    private var search: Search? = null
    private var mInterstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

    private fun init() {
        context = this
        dbModel = DBModel(context)
        txt_selected = findViewById(R.id.txt_selectedCount)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.home_timeline)
        Commons.isLogon(this)
        initAds()
        super.onCreate(savedInstanceState)
        search = Search.instance
        init()
        initListeners()
        Commons.refreshTweetList(this, tweetList_on_home)
        //        showAds();
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

    private fun initAds() {
        (findViewById<View>(R.id.adViewBanner) as AdView).loadAd(AdRequest.Builder().build())
        MobileAds.initialize(this) { createAndLoadRewardedAd() }
    }

    private fun initListeners() {
        chk_SelectAll!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { paramAnonymousCompoundButton, _ ->
            adapter = tweetList_on_home!!.adapter as TweetAdapter
            if (adapter == null) {
                try {
                    Commons.refreshTweetList(context as Activity, tweetList_on_home)
                    return@OnCheckedChangeListener
                } catch (e: Exception) {
                    Commons.showInfo(context, "Please click to Refresh!")
                    return@OnCheckedChangeListener
                }
            }
            var i = 0
            while (i < adapter!!.count) {
                val viewById = adapter!!
                        .getView(i, null, (findViewById<View>(R.id.tweetList_on_home) as ViewGroup))
                        ?.findViewById<CheckBox>(R.id.chkTweet)
                viewById?.isChecked = paramAnonymousCompoundButton.isChecked
                viewById?.callOnClick()
                i += 1
            }
            adapter!!.notifyDataSetChanged()
        })
        chk_RTs!!.setOnCheckedChangeListener { _, paramAnonymousBoolean ->
            search!!.isViewRTs = paramAnonymousBoolean
            Commons.refreshL(this@Main, dbModel, tweetList_on_home)
            chk_SelectAll!!.isChecked = false
        }
        chk_Mentions!!.setOnCheckedChangeListener { _, paramAnonymousBoolean ->
            search!!.isViewMentions = paramAnonymousBoolean
            Commons.refreshL(this@Main, dbModel, tweetList_on_home)
            chk_SelectAll!!.isChecked = false
        }
        chk_MyTweets!!.setOnCheckedChangeListener { _, paramAnonymousBoolean ->
            search!!.isViewMyTweets = paramAnonymousBoolean
            Commons.refreshL(this@Main, dbModel, tweetList_on_home)
            chk_SelectAll!!.isChecked = false
        }
    }

    fun doThings(paramView: View) {
        if (adapter == null) {
            adapter = tweetList_on_home?.adapter as TweetAdapter
        }
        showAds()
        when (paramView.id) {
            R.id.deleteTweet -> Commons.deleteSelected(this, adapter)
            R.id.refreshTweet -> Commons.refreshTweetList(this, tweetList_on_home)
            R.id.logOut -> Commons.logOut(this)
            else -> {
            }
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

    companion object {
        private var txt_selected: TextView? = null
        @JvmStatic
        fun selectedCountChange(isSelectedCount: Int) {
            txt_selected!!.text = isSelectedCount.toString()
        }
    }
}