package com.zsk.androtweet.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ListView
import android.widget.Toast
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.R
import com.zsk.androtweet.dialog.CustomDialog
import com.zsk.androtweet.models.Tweet
import twitter4j.Paging
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationBuilder

object Commons {
    private const val TAG = "ANDROTWEET"
    private val timelineDao = AndroTweetApp.database.timelineDao()
    var auth_dialog: Dialog? = null
    var oauth_url: String? = null
    var oauth_verifier: String? = null
    var pref_AndroTweet: SharedPreferences? = null
    lateinit var requestToken: RequestToken
    var web: WebView? = null

    @JvmOverloads
    fun showInfo(context: Context?, msg: String?, duration: Int = Toast.LENGTH_SHORT) {
        val toast = Toast.makeText(context, msg, duration)
        toast.duration = duration
        toast.setGravity(17, 0, 0)
        toast.show()
    }

    fun deleteSelected(context: Context, selectedTweets: List<Tweet>) {
        if (selectedTweets.isEmpty()) {
            Toast.makeText(context, "Please select tweets less one or more...", Toast.LENGTH_SHORT).show()
            return
        }
        val customDialog = CustomDialog(
                context,
                R.string.deleteTweets_title,
                R.string.deleteTweets_desc,
                R.string.twitter_clear_cache,
                0
        ).initOkButtonClickListener {
            it.dismiss()
        }.initActionButtonClickListener {
            it.dismiss()
        }
        DeleteSelectedTweets(
                getProgress(context, "Deleting Selected Tweets...", selectedTweets.size),
                customDialog
        ).execute(selectedTweets)

    }

    fun deleteSelected(context: Context, theAdapter: TweetAdapter?) {
        if (theAdapter == null || theAdapter.selectedCount == 0) {
            Toast.makeText(context, "Please select tweets less one or more...", Toast.LENGTH_SHORT).show()
            return
        }
        DeleteTweets(theAdapter, context).execute()
    }

    fun getProgress(context: Context?, title: String = "Progressing...", maxProgress: Int = 100, progressStyle: Int = ProgressDialog.STYLE_HORIZONTAL, message: String = ""): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle(title)
        progressDialog.max = maxProgress
        progressDialog.setProgressStyle(progressStyle)
        progressDialog.isIndeterminate = false
        progressDialog.setCanceledOnTouchOutside(false)
        if (message.isNotEmpty()) progressDialog.setMessage(message)
        return progressDialog
    }

    fun getTwitterObject(): Twitter {
        val localConfigurationBuilder = ConfigurationBuilder()
        localConfigurationBuilder.setOAuthConsumerKey(pref_AndroTweet!!.getString("CONSUMER_KEY", ""))
        localConfigurationBuilder.setOAuthConsumerSecret(pref_AndroTweet!!.getString("CONSUMER_SECRET", ""))
        localConfigurationBuilder.setDebugEnabled(true).setJSONStoreEnabled(true)
        val localAccessToken = AccessToken(pref_AndroTweet!!.getString("ACCESS_TOKEN", ""), pref_AndroTweet!!.getString("ACCESS_TOKEN_SECRET", ""))
        return TwitterFactory(localConfigurationBuilder.build()).getInstance(localAccessToken)
    }

    fun isLogon(context: Context) {
        if (pref_AndroTweet == null) {
            pref_AndroTweet = context.getSharedPreferences(TAG, 0)
        }
        val localEditor = pref_AndroTweet!!.edit()
        localEditor.putString("CONSUMER_KEY", "d2XSeF6fXPtVClaG3DALLjMT4")
        localEditor.putString("CONSUMER_SECRET", "e9UJ6mQuLK0pkFl4OTghjjHp1NR6PqkbdqwIiwgEIR9zEXTbQU")
        localEditor.apply()
        if ("" != pref_AndroTweet!!.getString("ACCESS_TOKEN", "")) {
            return
        }
//        timelineDao.deleteAll()
//        DBModel(context).deleteOldTweets()
        TokenGet(context).execute()
    }

    fun logOut(context: Context) {
        val localEditor = pref_AndroTweet!!.edit()
        localEditor.putString("ACCESS_TOKEN", "")
        localEditor.putString("ACCESS_TOKEN_SECRET", "")
        localEditor.apply()
//        DBModel(context).drop()
        isLogon(context)
    }

    fun refreshL(context: Context, tweets: List<Tweet>, timeLine: ListView) {
        if (tweets.isNotEmpty()) {
            val adapter = TweetAdapter(context, R.layout.tweets_layout, tweets)
            timeLine.adapter = adapter
        } else {
            timeLine.adapter = null
        }
    }

    @JvmOverloads
    fun refreshTweetList(activity: Activity, timeLine: ListView = activity.findViewById<View>(R.id.tweetList_on_home) as ListView, showProgressDialog: Boolean = true) {
        LoadTweets(activity, showProgressDialog, timeLine).execute()
        Toast.makeText(activity, "Refreshed...", Toast.LENGTH_SHORT).show()
    }

    internal class AccessTokenGet(private val context: Context, var twitter: Twitter) : AsyncTask<String, String, Boolean>() {
        private var progress: ProgressDialog? = null
        override fun onPreExecute() {
            super.onPreExecute()
            progress = getProgress(context)
            progress!!.show()
        }

        override fun doInBackground(vararg args: String): Boolean {
            try {
                val accessToken = twitter.getOAuthAccessToken(requestToken, oauth_verifier)
                val edit = pref_AndroTweet!!.edit()
                edit.putString("ACCESS_TOKEN", accessToken.token)
                edit.putString("ACCESS_TOKEN_SECRET", accessToken.tokenSecret)
                edit.putString("userName", twitter.screenName).apply()
                edit.apply()
            } catch (e: TwitterException) {
                e.printStackTrace()
            }
            return true
        }

        override fun onPostExecute(response: Boolean) {
            if (response) {
//                refreshTweetList(context as Activity)
                GetTweets(context).execute(true)
                progress!!.hide()
            }
        }

    }

    private class DeleteTweets(val adapter: TweetAdapter, val context: Context) : AsyncTask<Void, Tweet, Boolean>() {
        //        private val dbModel: DBModel = DBModel(context)
        private val selectedItems: Int = adapter.selectedCount
        private var progress: ProgressDialog? = null
        override fun onPreExecute() {
            if (selectedItems > 0) {
                progress = getProgress(context, "Deleting Selected Tweets...", selectedItems)
                progress!!.show()
            }
            super.onPreExecute()
        }

        override fun doInBackground(vararg voids: Void): Boolean {
            var isDone = false
            var r = 0
            if (selectedItems < 1) return false

            for (i in adapter.count - 1 downTo 0) {
                if (adapter.isSelectedPos[i]) {
                    r++
                    try {
                        adapter.getItem(i)?.let { tweet ->
                            try {
                                getTwitterObject().destroyStatus(tweet.id)
                                (context as Activity).runOnUiThread {
                                    progress!!.progress = r
                                    adapter.remove(tweet)
                                    adapter.isSelectedPos[i] = false
                                }
                                timelineDao.delete(tweet)
                                isDone = true
                            } catch (te: TwitterException) {
                            }

                            Thread.sleep(500)
                        }
                    } catch (e: TwitterException) {
                        Log.e(TAG, e.message)
                        e.printStackTrace()
                        return checkRateLimit()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
            return isDone
        }

        private fun checkRateLimit(): Boolean {
            try {
                val rateLimitStatus = getTwitterObject().rateLimitStatus
                for (endpoint in rateLimitStatus.keys) {
                    val status = rateLimitStatus[endpoint]
                    //                                log("Endpoint: " + endpoint);
                    if (status!!.remaining == 0) (context as Activity).runOnUiThread {
                        showInfo(context, endpoint + " limit exceeded! (" + status.remaining + "/" + status.limit + ") \n"
                                + "Please wait for " + status.secondsUntilReset / 60 + " minute(s)")
                    }
                    //                                log(" ResetTimeInSeconds: " + status.getResetTimeInSeconds());
//                                log(" SecondsUntilReset: " + status.getSecondsUntilReset());
                }
            } catch (e1: TwitterException) {
                e1.printStackTrace()
            }
            return false
        }

        override fun onPostExecute(aBoolean: Boolean) { //            DB.deleteOldTweets();
            if (selectedItems > 0) progress!!.dismiss()
            if (aBoolean) {
                adapter.notifyDataSetChanged()
                val customDialog = CustomDialog(
                        context,
                        R.string.deleteTweets_title,
                        R.string.deleteTweets_desc,
                        R.string.twitter_clear_cache,
                        0
                )
                customDialog.initOkButtonClickListener { it.dismiss() }
                customDialog.initActionButtonClickListener { it.dismiss() }
                //                refreshTweetList((Activity) context,false);
            }
            super.onPostExecute(aBoolean)
//            dbModel.close()
        }

        init {
            Log.e(TAG, selectedItems.toString())
        }
    }

    private class DeleteSelectedTweets(val progress: ProgressDialog, val finishDialog: CustomDialog) : AsyncTask<List<Tweet>, Int, Boolean>() {
        override fun onPreExecute() {
            progress.show()
            super.onPreExecute()
        }

        override fun doInBackground(vararg selectedTweets: List<Tweet>): Boolean {
            var r = 0
            selectedTweets[0]
                    .takeIf {
                        it.isNotEmpty()
                    }?.forEach {
                        try {
                            getTwitterObject().destroyStatus(it.id)
                            it.isSelected = false
                            it.isRemoved = true
                            timelineDao.setRemoved(it)
                            r++
                            onProgressUpdate(r)
                            Thread.sleep(500)
                        } catch (e: TwitterException) {
                        }
                    }
            return true
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            progress.progress = values[0] ?: 0
        }

        override fun onPostExecute(aBoolean: Boolean) {
            progress.dismiss()

            if (aBoolean) {
                finishDialog.show()
            }
            super.onPostExecute(aBoolean)
        }
    }

    class LoadTweets(private val context: Context, private val showProgressDialog: Boolean, private val timeLine: ListView) : AsyncTask<Void, Void, List<Tweet>>() {
        private var tweets: List<Tweet> = emptyList()
        //        private val dbModel: DBModel = DBModel(context)
        private val p = Paging()
        private var progress: ProgressDialog? = null
        override fun onPreExecute() {
            p.count = 150
            if (showProgressDialog) {
                progress = getProgress(context, "Loading Tweets...", p.count, ProgressDialog.STYLE_SPINNER)
                progress!!.show()
            }
            super.onPreExecute()
        }

        override fun doInBackground(vararg voids: Void): List<Tweet> {
            try {
                getTwitterObject().getUserTimeline(p).map {
                    Tweet(it)
                }.forEach {
                    timelineDao.insert(it)
                }

                tweets = timelineDao.allTweets

            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "UNKNOWN_ERROR")
                e.printStackTrace()
            }
            return tweets
        }

        override fun onPostExecute(tweets: List<Tweet>) { //            DB.close();
            if (showProgressDialog) progress!!.dismiss()
            refreshL(context, tweets, timeLine)
            super.onPostExecute(tweets)
        }

    }

    class GetTweets(private val context: Context, private val showProgressDialog: Boolean = true) : AsyncTask<Boolean, Void, Void>() {
        private val p = Paging()
        private var progress: ProgressDialog? = null
        override fun onPreExecute() {
            p.count = 150
            if (showProgressDialog) {
                progress = getProgress(context, "Loading Tweets...", p.count, ProgressDialog.STYLE_SPINNER)
                progress!!.show()
            }
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Boolean?): Void? {
            try {
                if (params[0] == true)
                    timelineDao.deleteAll()

                getTwitterObject().getUserTimeline(p).map {
                    Tweet(it)
                }.forEach {
                    timelineDao.insert(it)
                }

            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "UNKNOWN_ERROR")
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) { //            DB.close();
            if (showProgressDialog) progress!!.dismiss()
//            refreshL(context, tweets, timeLine)
            super.onPostExecute(result)
        }

    }

    internal class TokenGet(private val context: Context) : AsyncTask<String, String, String>() {
        var twitter: Twitter = TwitterFactory().instance
        override fun doInBackground(vararg args: String): String? {
            try {
                requestToken = twitter.oAuthRequestToken
                oauth_url = requestToken.authorizationURL
            } catch (e: TwitterException) {
                e.printStackTrace()
            }
            return oauth_url
        }

        @SuppressLint("SetJavaScriptEnabled")
        override fun onPostExecute(oauth_url: String?) {
            if (oauth_url != null) {
                Log.e("URL", oauth_url)
                auth_dialog = Dialog(context)
                auth_dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                auth_dialog!!.setContentView(R.layout.auth_dialog)
                web = auth_dialog!!.findViewById<View>(R.id.webv) as WebView
                web?.let {

                    it.settings.javaScriptEnabled = true
                    it.loadUrl(oauth_url)
                    it.webViewClient = object : WebViewClient() {
                        var authComplete = false

                        override fun onPageFinished(view: WebView, url: String) {
                            super.onPageFinished(view, url)
                            if (url.contains("oauth_verifier") && !authComplete) {
                                authComplete = true
                                Log.e("Url", url)
                                val uri = Uri.parse(url)
                                oauth_verifier = uri.getQueryParameter("oauth_verifier")
                                auth_dialog!!.dismiss()
                                AccessTokenGet(context, twitter).execute()
                            } else if (url.contains("denied")) {
                                auth_dialog!!.dismiss()
                                showInfo(context, "Sorry !, Permission Denied")
                            }
                        }
                    }
                }
                auth_dialog!!.show()
                auth_dialog!!.setCancelable(true)
            } else {
                showInfo(context, "Sorry !, Network Error or Invalid Credentials")
            }
        }

        init {
            twitter.setOAuthConsumer(pref_AndroTweet!!.getString("CONSUMER_KEY", ""), pref_AndroTweet!!.getString("CONSUMER_SECRET", ""))
        }
    }
}